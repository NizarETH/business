/**
 * 
 */
package com.euphor.paperpad.Beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author euphordev02
 *
 */
public class RelatedPageId extends RealmObject {
      @PrimaryKey
	private int id;
	private Related related;
	private int linked_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Related getRelated() {
        return related;
    }

    public void setRelated(Related related) {
        this.related = related;
    }

    public int getLinked_id() {
        return linked_id;
    }

    public void setLinked_id(int linked_id) {
        this.linked_id = linked_id;
    }

    public RelatedPageId() {
    }

    public RelatedPageId(int id, Related related, int linked_id) {
        this.id = id;
        this.related = related;
        this.linked_id = linked_id;
    }
}
