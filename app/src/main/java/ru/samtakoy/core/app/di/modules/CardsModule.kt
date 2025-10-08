package ru.samtakoy.core.app.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.samtakoy.features.database.data.MyRoomDb
import ru.samtakoy.features.tag.data.CardTagDao
import ru.samtakoy.features.tag.data.TagDao
import ru.samtakoy.features.card.data.CardsRepository
import ru.samtakoy.features.qpack.data.QPacksRepository
import ru.samtakoy.features.tag.data.TagsRepository
import ru.samtakoy.features.theme.data.ThemesRepository
import ru.samtakoy.features.card.data.CardsRepositoryImpl
import ru.samtakoy.features.qpack.data.QPacksRepositoryImpl
import ru.samtakoy.features.tag.data.TagsRepositoryImpl
import ru.samtakoy.features.theme.data.ThemesRepositoryImpl
import ru.samtakoy.features.card.domain.CardsInteractor
import ru.samtakoy.core.domain.FavoritesInteractor
import ru.samtakoy.features.card.domain.CardsInteractorImpl
import ru.samtakoy.core.domain.impl.FavoritesInteractorImpl
import ru.samtakoy.features.card.data.CardDao
import ru.samtakoy.features.card.data.mapper.CardEntityMapper
import ru.samtakoy.features.card.data.mapper.CardEntityMapperImpl
import ru.samtakoy.features.card.data.mapper.CardWithTagsEntityMapper
import ru.samtakoy.features.card.data.mapper.CardWithTagsEntityMapperImpl
import ru.samtakoy.features.qpack.data.QPackDao
import ru.samtakoy.features.qpack.data.mapper.QPackEntityMapper
import ru.samtakoy.features.qpack.data.mapper.QPackEntityMapperImpl
import ru.samtakoy.features.qpack.domain.QPackInteractor
import ru.samtakoy.features.qpack.domain.QPackInteractorImpl
import ru.samtakoy.features.tag.data.mapper.TagEntityMapper
import ru.samtakoy.features.tag.data.mapper.TagEntityMapperImpl
import ru.samtakoy.features.tag.domain.TagInteractor
import ru.samtakoy.features.tag.domain.TagInteractorImpl
import ru.samtakoy.features.theme.data.ThemeDao
import ru.samtakoy.features.theme.data.mapper.ThemeEntityMapper
import ru.samtakoy.features.theme.data.mapper.ThemeEntityMapperImpl
import ru.samtakoy.features.theme.domain.ThemeInteractor
import ru.samtakoy.features.theme.domain.ThemeInteractorImpl
import javax.inject.Singleton

@Module
internal interface CardsModule {
    @Binds
    fun bindsTagEntityMapper(impl: TagEntityMapperImpl): TagEntityMapper

    @Binds @Singleton
    fun bindsTagsRepository(impl: TagsRepositoryImpl): TagsRepository

    @Binds
    fun bindCardEntityMapper(impl: CardEntityMapperImpl): CardEntityMapper

    @Binds
    fun bindCardWithTagsEntityMapper(impl: CardWithTagsEntityMapperImpl): CardWithTagsEntityMapper

    @Binds @Singleton
    fun bindsCardsRepository(impl: CardsRepositoryImpl): CardsRepository

    @Binds
    fun bindThemeEntityMapper(impl: ThemeEntityMapperImpl): ThemeEntityMapper

    @Binds @Singleton
    fun bindsThemesRepository(impl: ThemesRepositoryImpl): ThemesRepository

    @Binds
    fun bindsQPackEntityMapper(impl: QPackEntityMapperImpl): QPackEntityMapper

    @Binds @Singleton
    fun bindsQPacksRepository(impl: QPacksRepositoryImpl): QPacksRepository

    @Binds
    fun bindsCardsInteractor(impl: CardsInteractorImpl): CardsInteractor

    @Binds
    fun bindsQPackInteractor(impl: QPackInteractorImpl): QPackInteractor

    @Binds
    fun bindsThemeInteractor(impl: ThemeInteractorImpl): ThemeInteractor

    @Binds
    fun bindsTagInteractor(impl: TagInteractorImpl): TagInteractor

    @Binds
    fun bindsFavoritesInteractor(impl: FavoritesInteractorImpl): FavoritesInteractor

    companion object {
        @JvmStatic
        @Provides
        fun providesTagDao(db: MyRoomDb): TagDao {
            return db.tagDao()
        }

        @JvmStatic
        @Provides
        fun providesCardTagDao(db: MyRoomDb): CardTagDao {
            return db.cardTagDao()
        }

        @JvmStatic
        @Provides
        fun providesQPackDao(db: MyRoomDb): QPackDao {
            return db.qPackDao()
        }

        @JvmStatic
        @Provides
        fun providesCardDao(db: MyRoomDb): CardDao {
            return db.cardDao()
        }

        @JvmStatic
        @Provides
        fun providesThemeDao(db: MyRoomDb): ThemeDao {
            return db.themeDao()
        }
    }
}
