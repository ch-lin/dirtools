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

package ch.dirtools.web.api;

import ch.platform.common.testing.DatabaseUnitTest;
import ch.dirtools.domain.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class DirToolControllerTest extends DatabaseUnitTest {

    @Autowired
    private MockMvc mvc;

    private final static String addItemURL = "/item/add";

    private final static String getItemURL = "/item/get";

    private final String itemName = "file.txt";

    private final String itemPath = "/Users/user/Desktop/";

    private final String crc32 = "0xD87F7E0C";

    private final String md5 = "098f6bcd4621d373cade4e832627b4f6";

    private final String sha1 = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";

    private final int OK = Response.Status.OK.getStatusCode();

    private final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();

    private final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();

    private Item createItem(String itemName, String itemPath, String crc32, String md5, String sha1) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setItemPath(itemPath);
        item.setCrc32(crc32);
        item.setMd5(md5);
        item.setSha1(sha1);
        return item;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MvcResult sendAddItem(Item item) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(addItemURL).contentType(MediaType.APPLICATION_JSON);
        if (item != null) {
            requestBuilder.content(asJsonString(item));
        }
        return mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON)).andReturn();
    }

    @Test
    public void addItem() throws Exception {
        Item item = createItem(itemName, itemPath, crc32, md5, sha1);
        MvcResult result = sendAddItem(item);
        int status = result.getResponse().getStatus();
        assertEquals(OK, status);
    }

    @Test
    public void addDuplicateItem() throws Exception {
        Item item = createItem(itemName, itemPath, crc32, md5, sha1);
        MvcResult result = sendAddItem(item);
        result.getResponse().getStatus();
        result = sendAddItem(item);
        int status = result.getResponse().getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    private void checkAddItemWithIncorrectParam(Item item) throws Exception {
        MvcResult result = sendAddItem(item);
        int status = result.getResponse().getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    public void addItemWithIncorrectParam() throws Exception {
        checkAddItemWithIncorrectParam(null);
        Item item = createItem(null, itemPath, crc32, md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem("", itemPath, crc32, md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, null, crc32, md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, "", crc32, md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, null, md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, "", md5, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, crc32, null, sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, crc32, "", sha1);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, crc32, md5, null);
        checkAddItemWithIncorrectParam(item);
        item = createItem(itemName, itemPath, crc32, md5, "");
        checkAddItemWithIncorrectParam(item);
    }

    private MvcResult sendGetItem(String itemName, String itemPath) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(getItemURL);
        if (itemName != null) {
            requestBuilder.param("name", itemName);
        }
        if (itemPath != null) {
            requestBuilder.param("path", itemPath);
        }
        requestBuilder.accept(MediaType.APPLICATION_JSON);
        return mvc.perform(requestBuilder).andReturn();
    }

    @Test
    public void getItem() throws Exception {
        Item item = createItem(itemName, itemPath, crc32, md5, sha1);
        sendAddItem(item);
        MvcResult result = sendGetItem(itemName, itemPath);
        int status = result.getResponse().getStatus();
        assertEquals(OK, status);
        Item itemResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Item.class);
        assertEquals(itemName, itemResult.getItemName());
        assertEquals(itemPath, itemResult.getItemPath());
    }

    @Test
    public void getNotExistItem() throws Exception {
        MvcResult result = sendGetItem(itemName, itemPath);
        int status = result.getResponse().getStatus();
        assertEquals(NOT_FOUND, status);
    }

    private void checkGetItemWithIncorrectParam(String itemName, String itemPath) throws Exception {
        MvcResult result = sendGetItem(itemName, itemPath);
        int status = result.getResponse().getStatus();
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    public void getItemWithIncorrectParam() throws Exception {
        checkGetItemWithIncorrectParam(itemName, null);
        checkGetItemWithIncorrectParam("", itemPath);
        checkGetItemWithIncorrectParam(null, itemPath);
        checkGetItemWithIncorrectParam(itemName, "");
    }

}