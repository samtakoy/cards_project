package ru.samtakoy.core.business.impl;

import android.content.ContentResolver;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import ru.samtakoy.core.business.impl.helpers.ZipHelper;
import ru.samtakoy.core.database.CardsDbSchema;
import ru.samtakoy.core.database.DbContentProvider;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Tag;
import ru.samtakoy.core.model.Theme;
import ru.samtakoy.core.model.utils.cbuild.CBuilderConst;
import ru.samtakoy.core.model.utils.cbuild.CardBuilder;
import ru.samtakoy.core.model.utils.cbuild.QPackBuilder;
import ru.samtakoy.core.services.import_utils.FromUriStreamFactory;
import ru.samtakoy.core.services.import_utils.FromZipEntryStreamFactory;
import ru.samtakoy.core.services.import_utils.ImportCardsException;
import ru.samtakoy.core.services.import_utils.ImportCardsOpts;
import ru.samtakoy.core.services.import_utils.StreamFactory;


public class ImportCardsHelper {

    private static final String TAG = "ImportCardsHelper";



    public static Observable<String> linesFromInputStream(
            final StreamFactory sf
            //InputStream iStream
    ){

        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {

                InputStream iStream = null;
                try {
                    iStream = sf.openStream();
                    InputStreamReader isr = new InputStreamReader(iStream, Charset.forName(ExportConst.FILES_CHARSET));
                    BufferedReader br = new BufferedReader(isr);

                    String line = br.readLine();
                    while (line != null){
                        observer.onNext(line);
                        line = br.readLine();
                    }
                    observer.onComplete();

                } catch (Exception e){

                    observer.onError(e);
                } finally {
                    try { if(iStream!=null){iStream.close();} }
                    catch (Exception ignored){}
                }
            }
        };
    }

    public static Observable<QPackBuilder> makeCardsBuilderFromFile(
            StreamFactory streamFactory,
            ImportCardsOpts opts
    ) {
        return makeCardsBuilderFromInputStream(streamFactory, opts).toObservable();

        /*
        return Observable.fromCallable(
                () -> resolver.openInputStream(selectedFileUri)
        ).concatMap(
                inputStream -> makeCardsBuilderFromInputStream(
                        resolver,
                        inputStream,
                        targetThemeId,
                        selectedFileUri.getPath(),
                        UriUtils.getFileNameByUri(resolver, selectedFileUri)
                ).toObservable()
        );/***/
    }

    /*
    public static Single<QPackBuilder> makeCardsBuilderFromFile(
            ContentResolver resolver, Long targetThemeId, Uri selectedFileUri
    ) {
        return Observable.create(
                (ObservableOnSubscribe<InputStream>) emitter ->
                {
                    emitter.onNext(resolver.openInputStream(selectedFileUri));
                    emitter.onComplete();
                }
        )
        .concatMap(inputStream -> linesFromInputStream(inputStream))
        .collect(
                ()->new QPackBuilder(
                        targetThemeId,
                        selectedFileUri.getPath(),
                        ContentProviderHelper.buildTagMap(resolver),
                        UriUtils.getFileNameByUri(resolver, selectedFileUri)
                ),
                QPackBuilder::addLine
        );
    }/**/

    public static Single<QPackBuilder> makeCardsBuilderFromInputStream(
            StreamFactory streamFactory,
            ImportCardsOpts opts
    ) {
        return linesFromInputStream(streamFactory)
                .collect(
                        ()->new QPackBuilder(
                                streamFactory.getThemeId(),
                                streamFactory.getSrcPath(),
                                ContentProviderHelper.buildTagMap(streamFactory.getResolver()),
                                streamFactory.getFileName(),
                                opts.getNullifyId()
                        ),
                        QPackBuilder::addLine
                );
    }

    /*
    public static Completable loadCardsFromFile(
            ContentResolver resolver,
            Uri selectedFileUri,
            Long targetThemeId,
            ImportCardsOpts opts
    ) {
        return Single.fromCallable(() -> !ContentProviderHelper.isAnyPackExists(resolver))
                .flatMapCompletable(
                        allowByIdCreation ->
                        loadCardsFromFile(resolver, selectedFileUri, targetThemeId, opts)
                );
    }/***/

    public static Completable loadCardsFromFile(
            ContentResolver resolver,
            Uri selectedFileUri,
            Long targetThemeId,
            ImportCardsOpts opts
    ) {
        // TODO транзакции
        FromUriStreamFactory streamFactory = new FromUriStreamFactory(resolver, targetThemeId, selectedFileUri);
        return makeCardsBuilderFromFile(streamFactory, opts)

                .filter(qPackBuilder -> isAllowedByOpts(qPackBuilder, opts))

                .map(qPackBuilder -> serializePack(resolver, qPackBuilder, opts) )



                .map(qPackBuilder -> qPackBuilder.build() )

                .flatMap(qPackBuilder -> Observable.fromIterable(qPackBuilder.getCardBuilders()))
                .map(card -> serializeCard(resolver, card))

                .ignoreElements();
    }

    private static boolean isAllowedByOpts(QPackBuilder qPackBuilder, ImportCardsOpts opts) {
        if(qPackBuilder.hasIncomingId()){
            // с id
            return opts.isAllowWithIdProcessing();
        } else {
            // без id
            return opts.isAllowNewImporting();
        }
    }

    public static Observable<QPackBuilder> makeQPackBuildersFromZip(
            ContentResolver resolver,
            Uri zipFileUri,
            ImportCardsOpts opts
    ) {
        return Observable.fromCallable(() -> resolver.openInputStream(zipFileUri))
                .concatMap(stream -> ZipHelper.unzipStream(resolver, stream))
                .map(streamFactory -> actualizeThemes(streamFactory))
                .concatMap(streamFactory -> makeCardsBuilderFromFile(streamFactory, opts));
    }

    private static Observable<QPackBuilder> makeQPackBuildersFromPath(
            ContentResolver resolver,
            String dirPath,
            Long targetThemeId,
            ImportCardsOpts opts
    ){
        return Observable.fromArray(new File(dirPath).listFiles())
                .flatMap(f1->listFiles(resolver, f1, targetThemeId, opts));
    }

    private static Completable processImportFromObservables(
            ContentResolver resolver,
            Observable<QPackBuilder> qpBuilders,
            ImportCardsOpts opts
    ){
        return qpBuilders.scan((prevBuilder, nextBuilder) -> {
            nextBuilder.setBuilderNum(prevBuilder.getBuilderNum()+1);
            return nextBuilder;
        })


                .toSortedList(
                        (aBuilder, bBuilder) -> {
                            if(aBuilder.hasIncomingId() == bBuilder.hasIncomingId()){
                                return 0;
                            }
                            if(aBuilder.hasIncomingId()){
                                // с id раньше обрабатываются
                                return -1;
                            }
                            // без id в хвосте
                            return 1;
                        }
                )
                //.collectInto(new ArrayList<QPackBuilder>(), (builders, qPackBuilder) -> builders.add(qPackBuilder))
                .toObservable()

                .flatMap(
                        builders -> Observable.fromIterable(builders),
                        (builders, qPackBuilder) -> {
                            qPackBuilder.setBuildersCount(builders.size());
                            return qPackBuilder;
                        }
                )

                .filter(qPackBuilder -> isAllowedByOpts(qPackBuilder, opts))
                .map(qPackBuilder -> serializePack(resolver, qPackBuilder, opts) )
                .map(qPackBuilder -> qPackBuilder.build() )
                .concatMap(qPackBuilder -> Observable.fromIterable(qPackBuilder.getCardBuilders()))
                .map(card -> serializeCard(resolver, card))
                .ignoreElements();
    }

    public static Completable batchLoadFromFolder(
            ContentResolver resolver,
            String dirPath,
            Long targetThemeId,
            ImportCardsOpts opts
    ) {
        return processImportFromObservables(resolver, makeQPackBuildersFromPath(resolver, dirPath, targetThemeId, opts), opts);

        /*
        return Single.fromCallable(() -> !ContentProviderHelper.isAnyPackExists(resolver))
        .flatMapCompletable(
                allowByIdCreation ->
                processImportFromObservables(resolver, makeQPackBuildersFromPath(resolver, dirPath, targetThemeId), allowByIdCreation)
        );/***/
    }

    public static Completable batchUpdateFromZip(
            ContentResolver resolver,
            Uri zipFileUri,
            ImportCardsOpts opts
    ) {
        return processImportFromObservables(resolver, makeQPackBuildersFromZip(resolver, zipFileUri, opts), opts);

        /*
        return Single.fromCallable(() -> !ContentProviderHelper.isAnyPackExists(resolver))
        .flatMapCompletable(
                allowByIdCreation ->
                        processImportFromObservables(resolver, makeQPackBuildersFromZip(resolver, zipFileUri), allowByIdCreation)
        );/***/
    }


    private static Card serializeCard(ContentResolver resolver, CardBuilder cardBuilder) throws ImportCardsException {

//MyLog.add("serializeCard:"+card.getId()+", qPack:"+card.getQuestion());

        Card card = cardBuilder.build();

        if(cardBuilder.isToRemove()){

            // проверить, что карта из нашего пака

            ContentProviderHelper.deleteCard(resolver, cardBuilder.getId());
            // TODO rx null не ест?
            return card;
        }

        if(cardBuilder.hasId()){

            Card existingCard = ContentProviderHelper.getDummyIdCard(resolver, card.getId());
            if(existingCard != null){

                // обновить
                if(existingCard.getQPackId()  == cardBuilder.getQPackId()){
                    resolver.update(
                            DbContentProvider.CONTENT_URI_CARDS,
                            card.getContentValues(true),
                            CardsDbSchema.Cards.Cols.ID+" = ?",
                            new String[]{String.valueOf(cardBuilder.getId())}
                    );
                } else {
                    throw new ImportCardsException(ImportCardsException.ERR_WRONG_CARD_PACK, "");
                }

                // будет обновлено ниже
                ContentProviderHelper.deleteCardTagsByCardId(resolver, cardBuilder.getId());
            } else {
                // создать
                Uri cardUri = resolver.insert(DbContentProvider.CONTENT_URI_CARDS, card.getContentValues(false));

                Long cardId = Long.parseLong(cardUri.getLastPathSegment());
                cardBuilder.setId(cardId);
                card.setId(cardId);
            }
        } else{
            Uri cardUri = resolver.insert(DbContentProvider.CONTENT_URI_CARDS, card.getContentValues(false));
            Long cardId = Long.parseLong(cardUri.getLastPathSegment());
            cardBuilder.setId(cardId);
            card.setId(cardId);
        }

        for(Tag tag:card.getTags()){
            serializeCardTag(resolver, tag);
        }
        ContentProviderHelper.addCardTags(resolver, card.getId(), card.getTags());
        return card;
    }

    private static Tag serializeCardTag(ContentResolver resolver, Tag tag) {
        if(!tag.hasId()){
            Uri tagUri = resolver.insert(DbContentProvider.CONTENT_URI_TAGS, tag.getContentValues(false));
            tag.setId(Long.parseLong(tagUri.getLastPathSegment()));
        }
        return tag;
    }

    private static QPackBuilder serializePack(
            ContentResolver resolver,
            QPackBuilder qPackBuilder,
            ImportCardsOpts opts) throws ImportCardsException {

        QPack qPack = QPack.createNew(
                qPackBuilder.getThemeId(),
                qPackBuilder.getSrcFilePath(),
                qPackBuilder.getFileName(),
                qPackBuilder.getTitle(),
                qPackBuilder.getDesc()
        );
        if(qPackBuilder.hasCreationDate()){
            qPack.parseCreationDateFromString(qPackBuilder.getCreationDate());
            qPack.setLastViewDateMillis(qPack.getCreationDateAsLong());
        }
        if(qPackBuilder.hasViewCount()){
            qPack.setViewCount(qPackBuilder.getViewCount());
        }

        if(qPackBuilder.hasIncomingId()){

            qPack.setId(qPackBuilder.getParsedId());
            if(ContentProviderHelper.isPackExists(resolver, qPackBuilder.getParsedId())){
                // обновить
                ContentProviderHelper.saveQPack(resolver, qPack);
            } else {
                // создать новый, если разрешено
                if(opts.isAllowWithIdCreation()){
                    resolver.insert(DbContentProvider.CONTENT_URI_QPACKS, qPack.getContentValues(true));
                } else {
                    throw new ImportCardsException(ImportCardsException.ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED, "");
                }
            }

        } else{

            Uri qPackUri = resolver.insert(DbContentProvider.CONTENT_URI_QPACKS, qPack.getContentValues(false));
            qPack.setId(Long.parseLong(qPackUri.getLastPathSegment()));
        }

        qPackBuilder.setTargetQPack(qPack.getId());
        return qPackBuilder;
    }

    // ==========================================================

    public static boolean isPackFile(File file){
        return isPackFile(file.getName());
    }

    public static boolean isPackFile(String fileName){
        return getExtension(fileName).equals("txt");
    }

    private static String getExtension(String fileName){
        int idx = fileName.lastIndexOf(".");
        if(idx > 0){
            return fileName.substring(idx+1);
        }
        return "";
    }

    private static boolean isValidThemeFolderName(String folderName){
        return folderName.indexOf(".") != 0;
    }

    private static Observable<QPackBuilder> listFiles(
            ContentResolver resolver,
            @NotNull File f,
            Long parentThemeId,
            ImportCardsOpts opts
    ) {

        if(f.isDirectory()) {

            if(!isValidThemeFolderName(f.getName())){
                return Observable.empty();
            }

            // создать или получить соответствуюущую дирректорию в базе
            // получить из базы - дочернюю с тем же именем, как дирректория
            // если нет - создать и ее id транслировать ниже

            long childThemeId;
            Theme childTheme = ContentProviderHelper.getThemeWithName(resolver, parentThemeId, f.getName());
            if(childTheme != null){
                childThemeId = childTheme.getId();
            } else{
                childThemeId = ContentProviderHelper.addNewTheme(resolver, parentThemeId, f.getName());
            }
            return Observable.fromArray(f.listFiles()).flatMap(file -> listFiles(resolver, file, childThemeId, opts));
        }
        if(!isPackFile(f)){
            return Observable.empty();
        }

        FromUriStreamFactory streamFactory = new FromUriStreamFactory(resolver, parentThemeId, Uri.fromFile(f));
        return makeCardsBuilderFromFile(streamFactory, opts);//.toObservable();
    }




    private static FromZipEntryStreamFactory actualizeThemes(FromZipEntryStreamFactory streamFactory) {

        List<String> themesList = streamFactory.getThemesPath();

        if(themesList.size() == 0){
            // hasnt parent theme
            streamFactory.setThemeId(CBuilderConst.NO_ID);
            return streamFactory;
        }

        Long parentThemeId = CBuilderConst.NO_ID;
        boolean newTheme = false;
        ContentResolver resolver = streamFactory.getResolver();

        for(String themeName:themesList){
            Theme theme =
                    newTheme
                        ? null
                        : ContentProviderHelper.getThemeWithName(resolver, parentThemeId, themeName);
            if(theme != null){
                parentThemeId = theme.getId();
            } else{
                newTheme = true;
                parentThemeId = ContentProviderHelper.addNewTheme(resolver, parentThemeId, themeName);
            }
        }
        streamFactory.setThemeId(parentThemeId);
        return streamFactory;
    }




    /*


    private void batchImportFromDir(File dirFile){
        listFiles(dirFile)
                .filter(this::isPackFile)
                .map(file -> new FileInputStream(file))
                .flatMap(
                        iStream -> linesFromInputStream(iStream)
                                .collect(
                                        ()->new QPackBuilder(
                                                targetThemeId,
                                                selectedFileUri,
                                                ContentProviderHelper.buildTagMap(resolver),
                                                UriUtils.getFileNameByUri(callerContext, selectedFileUri)
                                        ),
                                        QPackBuilder::addLine
                                );
                        ,

                );
    }

/***/


    // ==========================================================


    private static class FileInfo{

        private ContentResolver mResolver;
        private Long mTargetThemeId;
        private Uri mSelectedFileUri;

        FileInfo(ContentResolver resolver, Long targetThemeId, Uri selectedFileUri){
            mResolver = resolver;
            mTargetThemeId = targetThemeId;
            mSelectedFileUri = selectedFileUri;
        }

        public ContentResolver getResolver() {
            return mResolver;
        }

        public Long getTargetThemeId() {
            return mTargetThemeId;
        }

        public Uri getSelectedFileUri() {
            return mSelectedFileUri;
        }
    }

    // ==========================================================

}
