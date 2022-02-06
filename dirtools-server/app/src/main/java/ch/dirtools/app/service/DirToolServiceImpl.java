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
import ch.dirtools.common.exception.ItemExistException;
import ch.dirtools.common.exception.ItemNotFoundException;
import ch.dirtools.domain.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirToolServiceImpl implements DirToolService {

    @Autowired
    public ItemDao itemDao;

    public void addItem(Item item) throws ItemExistException {
        if (inValidate(item)) {
            throw new IllegalArgumentException();
        }
        itemDao.addItem(item);
    }

    public Item getItem(String itemName, String itemPath) throws ItemNotFoundException {
        if (itemName == null || itemName.isBlank() || itemPath == null || itemPath.isBlank()) {
            throw new IllegalArgumentException();
        }
        return itemDao.getItem(itemName, itemPath);
    }

    public List<Item> getAllItems() {
        return itemDao.getAllItems();
    }

    public int cleanup() {
        return itemDao.cleanup();
    }

    private boolean inValidate(final Item item) {
        return item == null || item.getItemName() == null || item.getItemName().isBlank()
                || item.getItemPath() == null || item.getItemPath().isBlank()
                || item.getCrc32() == null || item.getCrc32().isBlank() ||
                item.getMd5() == null || item.getMd5().isBlank() ||
                item.getSha1() == null || item.getSha1().isBlank();
    }

}