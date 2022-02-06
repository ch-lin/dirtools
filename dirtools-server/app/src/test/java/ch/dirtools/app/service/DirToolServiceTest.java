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

package ch.dirtools.app.service;

import ch.dirtools.app.dao.ItemDao;
import ch.dirtools.app.dao.ItemDaoImpl;
import ch.dirtools.common.reply.ComparedStatusReply;
import ch.platform.common.testing.DatabaseUnitTest;
import ch.dirtools.common.exception.ItemExistException;
import ch.dirtools.common.exception.ItemNotFoundException;
import ch.dirtools.domain.model.Item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirToolServiceTest extends DatabaseUnitTest {

    private final String itemName = "file.txt";

    private final String itemPath = "/Users/user/Desktop/";

    private final String crc32 = "0xD87F7E0C";

    private final String md5 = "098f6bcd4621d373cade4e832627b4f6";

    private final String sha1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";

    private final ItemDao itemDao = new ItemDaoImpl();

    private final DirToolServiceImpl dirToolService = new DirToolServiceImpl();

    @BeforeEach
    protected void setup() throws ItemExistException {
        dirToolService.itemDao = itemDao;
        Item item = createItem(itemName, itemPath, crc32, md5, sha1);
        dirToolService.addItem(item);
    }

    private Item createItem(String itemName, String itemPath, String crc32, String md5, String sha1) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setItemPath(itemPath);
        item.setCrc32(crc32);
        item.setMd5(md5);
        item.setSha1(sha1);
        return item;
    }

    @Test
    public void testAddItem() throws ItemExistException {
        final String _itemName = "file2.txt";
        final String _itemPath = "/Users/user/Desktop/Temp/";
        final String _crc32 = "_crc32";
        final String _md5 = "_md5";
        final String _sha1 = "_sha1";
        Item item = createItem(_itemName, _itemPath, _crc32, _md5, _sha1);
        dirToolService.addItem(item);
        List<Item> results = dirToolService.getAllItems();
        assertEquals(2, results.size());
        assertEquals(item, results.get(1));
    }

    private void checkAddItemWithIncorrectParam(String itemName, String itemPath, String crc32, String md5, String sha1) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Item item = createItem(itemName, itemPath, crc32, md5, sha1);
            dirToolService.addItem(item);
        });
    }

    @Test
    public void testAddItemWithIncorrectParam() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dirToolService.addItem(null)
        );
        checkAddItemWithIncorrectParam(null, itemPath, crc32, md5, sha1);
        checkAddItemWithIncorrectParam("", itemPath, crc32, md5, sha1);
        checkAddItemWithIncorrectParam(itemName, null, crc32, md5, sha1);
        checkAddItemWithIncorrectParam(itemName, "", crc32, md5, sha1);
        checkAddItemWithIncorrectParam(itemName, itemPath, null, md5, sha1);
        checkAddItemWithIncorrectParam(itemName, itemPath, "", md5, sha1);
        checkAddItemWithIncorrectParam(itemName, itemPath, crc32, null, sha1);
        checkAddItemWithIncorrectParam(itemName, itemPath, crc32, "", sha1);
        checkAddItemWithIncorrectParam(itemName, itemPath, crc32, md5, null);
        checkAddItemWithIncorrectParam(itemName, itemPath, crc32, md5, "");
    }

    @Test
    public void testAddDuplicateItem() {
        final String itemName2 = itemName;
        final String itemPath2 = itemPath;
        Assertions.assertThrows(ItemExistException.class, () -> {
            Item item = createItem(itemName2, itemPath2, crc32, md5, sha1);
            dirToolService.addItem(item);
        });
    }

    @Test
    public void testGetItem() throws ItemNotFoundException {
        Item result = dirToolService.getItem(itemName, itemPath);
        assertNotNull(result);
        assertEquals(itemName, result.getItemName());
    }

    private void checkGetItemWithIncorrectParam(String itemName, String itemPath) {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dirToolService.getItem(itemName, itemPath)
        );
    }

    @Test
    public void testGetItemWithIncorrectParam() {
        checkGetItemWithIncorrectParam(null, itemPath);
        checkGetItemWithIncorrectParam("", itemPath);
        checkGetItemWithIncorrectParam(itemName, null);
        checkGetItemWithIncorrectParam(itemName, "");
    }

    @Test
    public void testGetItemNotFound() {
        final String itemName2 = itemName + "_";
        Assertions.assertThrows(ItemNotFoundException.class, () ->
                dirToolService.getItem(itemName2, itemPath)
        );
    }

    @Test
    public void testCleanup() {
        List<Item> results = dirToolService.getAllItems();
        assertTrue(results.size() > 0);
        dirToolService.cleanup();
        results = dirToolService.getAllItems();
        assertEquals(0, results.size());
    }

    @Test
    public void testCompare() throws ItemNotFoundException {
        final Item itemInfo = createItem(itemName, itemPath, crc32, md5, sha1);
        assertEquals(ComparedStatusReply.Status.OK, dirToolService.compareItem(itemInfo).getStatus());
    }

    @Test
    public void testCompareModifiedItem() throws ItemNotFoundException {
        final String differentItemSHA1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd2";
        Item itemInfo2 = createItem(itemName, itemPath, crc32, md5, differentItemSHA1);
        assertEquals(ComparedStatusReply.Status.MODIFIED, dirToolService.compareItem(itemInfo2).getStatus());
    }

    @Test
    public void testCompareNonExistItemInfo() {
        final String differentItemName = itemName + "_";
        Item itemInfo2 = createItem(differentItemName, itemPath, crc32, md5, sha1);
        Assertions.assertThrows(ItemNotFoundException.class, () ->
                dirToolService.compareItem(itemInfo2)
        );
    }

    private void checkCompareItemWithIncorrectParam(String itemName, String itemPath, String crc32, String md5, String sha1) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Item item = createItem(itemName, itemPath, crc32, md5, sha1);
            dirToolService.compareItem(item);
        });
    }

    @Test
    public void testCompareWithIncorrectParam() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                dirToolService.compareItem(null)
        );
        checkCompareItemWithIncorrectParam(null, itemPath, crc32, md5, sha1);
        checkCompareItemWithIncorrectParam("", itemPath, crc32, md5, sha1);
        checkCompareItemWithIncorrectParam(itemName, null, crc32, md5, sha1);
        checkCompareItemWithIncorrectParam(itemName, "", crc32, md5, sha1);
        checkCompareItemWithIncorrectParam(itemName, itemPath, null, md5, sha1);
        checkCompareItemWithIncorrectParam(itemName, itemPath, "", md5, sha1);
        checkCompareItemWithIncorrectParam(itemName, itemPath, crc32, null, sha1);
        checkCompareItemWithIncorrectParam(itemName, itemPath, crc32, "", sha1);
        checkCompareItemWithIncorrectParam(itemName, itemPath, crc32, md5, null);
        checkCompareItemWithIncorrectParam(itemName, itemPath, crc32, md5, "");
    }

}