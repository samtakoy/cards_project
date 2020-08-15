package ru.samtakoy.core.model.elements;

import android.content.res.Resources;

import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.TreeSet;

import ru.samtakoy.core.model.utils.MyStringUtils;

public class Schedule {

    private static final String ID_DELIMITTER = ",";

    //public static final Schedule EMPTY = new Schedule();

    public static final Schedule DEFAULT = new Schedule()
            //.addItem(new ScheduleItem(1, ScheduleTimeUnit.MINUTE))

            .addItem(new ScheduleItem(30, ScheduleTimeUnit.MINUTE))
            .addItem(new ScheduleItem(1, ScheduleTimeUnit.HOUR))
            .addItem(new ScheduleItem(2, ScheduleTimeUnit.HOUR))
            .addItem(new ScheduleItem(4, ScheduleTimeUnit.HOUR))
            .addItem(new ScheduleItem(8, ScheduleTimeUnit.HOUR))
            .addItem(new ScheduleItem(1, ScheduleTimeUnit.DAY))
            .addItem(new ScheduleItem(3, ScheduleTimeUnit.DAY))
            .addItem(new ScheduleItem(1, ScheduleTimeUnit.WEEK))
            .addItem(new ScheduleItem(2, ScheduleTimeUnit.WEEK))
            .addItem(new ScheduleItem(1, ScheduleTimeUnit.MONTH))
            .addItem(new ScheduleItem(2, ScheduleTimeUnit.MONTH))
            .addItem(new ScheduleItem(6, ScheduleTimeUnit.MONTH))
            .addItem(new ScheduleItem(1, ScheduleTimeUnit.YEAR))
            ;


    public static Schedule createEmpty() {
        return new Schedule();
    }

    public static Schedule fromString(String srcString){
        Schedule result = new Schedule();
        result.initFromString(srcString);
        return result;
    }

    private TreeSet<ScheduleItem> mItems;

    public Schedule(){
        //mTitleId = titleId;
        mItems = createItems();
    }



    public Schedule addItem(ScheduleItem item){
        mItems.add(item.copy());
        return this;
    }

    public Schedule mergeItem(ScheduleItem item){
        for(ScheduleItem curItem:mItems){
            if(curItem.getTimeUnit() == item.getTimeUnit()){
                curItem.deltaDimension(item.getDimension());
                return this;
            }
        }
        mItems.add(item.copy());
        return this;
    }

    public void initFromString(String srcString){
        mItems = createItems();

        if(srcString==null || srcString.length()==0){
            return;
        }

        String[] parts = StringUtils.split(srcString, ID_DELIMITTER);
        for(String oneStringPart:parts){
            ScheduleItem item = ScheduleItem.fromString(oneStringPart);
            if(item == null){
                throw new RuntimeException("unknown shedule item while string parsing");
            }
            mItems.add(item);
        }
    }

    private TreeSet<ScheduleItem> createItems() {
        return new TreeSet<>();
    }

    @Override
    public String toString() {
        return MyStringUtils.join(mItems, ",");
    }

    public String toStringViewWithPrev(Resources resources, Schedule prevSchedule){
        return "("+prevSchedule.getLastItem()+")"+toStringView(resources);
    }

    public String toStringView(Resources resources){
        StringBuilder sb = new StringBuilder();
        Iterator<ScheduleItem> itr = mItems.iterator();
        while(itr.hasNext()){
            ScheduleItem item = itr.next();
            if(sb.length() != 0) {
                sb.append(" ");
            }
            sb.append(item.toStringView(resources));
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return mItems.size() == 0;
    }

    public void clear() {
        mItems.clear();
    }

    public ScheduleItem getFirstItem() {
        if(isEmpty()){
            return null;
        }
        return mItems.first();
    }

    public ScheduleItem getLastItem() {
        if(isEmpty()){
            return null;
        }
        return mItems.last();
    }

    public int extractFirstItemInMillis() {
        if(isEmpty()){
            return 0;
        }
        ScheduleItem item = mItems.pollFirst();
        return item.getMillis();
    }

    public Schedule copy() {
        Schedule result = new Schedule();

        for(ScheduleItem item:mItems){
            result.addItem(item.copy());
        }

        return result;
    }
}
