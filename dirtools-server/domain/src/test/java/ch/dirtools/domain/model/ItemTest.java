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

import ch.platform.common.testing.DatabaseUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest extends DatabaseUnitTest {

    private final String itemName = "file.txt";

    private final String itemPath = "/Users/user/Desktop/";

    private final String crc32 = "0xD87F7E0C";

    private final String md5 = "098f6bcd4621d373cade4e832627b4f6";

    private final String sha1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";

    private Item item;

    @BeforeEach
    public void prepare() {
        item = new Item();
        item.setItemName(itemName);
        item.setItemPath(itemPath);
        item.setCrc32(crc32);
        item.setMd5(md5);
        item.setSha1(sha1);
        em.persist(item);
    }

    @Test
    public void testSave() {
        final String newItemName = "file2.txt";
        Item item2 = new Item(newItemName, itemPath, crc32, md5, sha1);
        em.persist(item2);
        TypedQuery<Item> query = em.createQuery("SELECT e FROM Item e WHERE e.itemName = :itemName", Item.class);
        List<Item> result = query.setParameter("itemName", newItemName).getResultList();
        assertEquals(1, result.size());
        assertEquals(newItemName, result.get(0).getItemName());
    }

    @Test
    public void testLoad() {
        TypedQuery<Item> query = em.createQuery("SELECT e FROM Item e", Item.class);
        List<Item> result = query.getResultList();
        assertEquals(1, result.size());
        assertEquals(itemName, result.get(0).getItemName());
        assertEquals(itemPath, result.get(0).getItemPath());
        assertEquals(crc32, result.get(0).getCrc32());
        assertEquals(md5, result.get(0).getMd5());
        assertEquals(sha1, result.get(0).getSha1());
        assertTrue(result.get(0).getItemID() > 0);
    }

    @Test
    public void testSaveNotNullOnItemName() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Item item2 = new Item(null, itemPath, crc32, md5, sha1);
            em.persist(item2);
            em.flush();
        });
    }

    @Test
    public void testSaveNotNullOnItemPath() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Item item2 = new Item(itemName, null, crc32, md5, sha1);
            em.persist(item2);
            em.flush();
        });
    }

    @Test
    public void testEqual() {
        Item item2 = new Item(itemName, itemPath, crc32, md5, sha1);
        assertEquals(item, item);
        assertEquals(item, item2);
        assertEquals(item.hashCode(), item2.hashCode());
    }

    @Test
    public void testNotEqual() {
        Item item1 = new Item(itemName + "_", itemPath, crc32, md5, sha1);
        Item item2 = new Item(itemName, itemPath + "_", crc32, md5, sha1);
        Item item3 = new Item(itemName, itemPath, crc32 + "_", md5, sha1);
        Item item4 = new Item(itemName, itemPath, crc32, md5 + "_", sha1);
        Item item5 = new Item(itemName, itemPath, crc32, md5, sha1 + "_");
        assertNotEquals(item, null);
        assertNotEquals(item, item1);
        assertNotEquals(item, item2);
        assertNotEquals(item, item3);
        assertNotEquals(item, item4);
        assertNotEquals(item, item5);
        assertNotEquals(item.hashCode(), item2.hashCode());
        class Item2 extends Item {
        }
        assertNotEquals(new Item2(), item);
    }

}