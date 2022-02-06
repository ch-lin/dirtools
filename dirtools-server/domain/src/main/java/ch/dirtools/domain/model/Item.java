/*=============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Che-Hung Lin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *===========================================================================*/

package ch.dirtools.domain.model;

import javax.persistence.*;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static ch.dirtools.domain.model.Item.*;

@Table(name = TABLE_NAME, indexes = {@Index(name = ITEM_ID_IDX, columnList = ITEM_ID)},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"itemName", "itemPath"})})
@Entity
public class Item {

    public static final String TABLE_NAME = "Item";
    public static final String ITEM_ID = "ItemID";
    public static final String ITEM_ID_IDX = "item_id_index";

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ITEM_ID)
    private long itemID;

    @Getter
    @Setter
    @NotNull
    private String itemName = null;

    @Getter
    @Setter
    @NotNull
    private String itemPath = null;

    @Getter
    @Setter
    private String crc32;

    @Getter
    @Setter
    private String md5;

    @Getter
    @Setter
    private String sha1;

    public Item() {
    }

    public Item(String itemName, String itemPath, String crc32, String md5, String sha1) {
        this.itemName = itemName;
        this.itemPath = itemPath;
        this.crc32 = crc32;
        this.md5 = md5;
        this.sha1 = sha1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equal(itemName, item.itemName) && Objects.equal(itemPath, item.itemPath) && Objects.equal(crc32, item.crc32) && Objects.equal(md5, item.md5) && Objects.equal(sha1, item.sha1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemName, itemPath, crc32, md5, sha1);
    }

}