package com.euphor.paperpad.utils;

import android.content.Context;

import com.euphor.paperpad.Beans.Category;
import com.euphor.paperpad.Beans.Child_pages;
import com.euphor.paperpad.Beans.Section;
import com.euphor.paperpad.activities.main.MainActivity;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by euphor on 14/12/15.
 */
public class CategoryTo {

    public static Section Sections(Category category, Context context) {
        Realm r =  Realm.getInstance(context);

        RealmResults<Section> sections = r.where(Section.class).findAll();
        Section section = null;
        Iterator<Section> iterator = sections.iterator();
        while (iterator.hasNext()) {
            Section s = iterator.next();
            for (int i = 0; i <s.getCategories().size() ; i++) {
                int id = s.getCategories().get(i).getId();
                if(id == category.getId())
                    section = r.where(Section.class).equalTo("id",s.getId()).findFirst();
            }
        }

        return section;
    }
    public static int getParent(Child_pages page, Context context)
    {
        Realm r = Realm.getInstance(context);
        int num = 0;
        List<Category> categories = r.where(Category.class).findAll();
        for (int i = 0; i <categories.size() ; i++) {

            if(categories.get(i).getId() ==  page.getCategory_id())
                num = categories.get(i).getCart_parent_category();
        }
        return num;

    }
}
