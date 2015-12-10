package fr.iut.obj2json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import junit.framework.Assert;

public class Obj2JSonConverterTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testConvertAny() throws IllegalArgumentException, IllegalAccessException, IOException {
		String res = Obj2JSonConverter.convertObject(new MockObj());
        String expected = new String(Files.readAllBytes(Paths.get("F:\\Users\\Bla\\workspace\\Json_Anthony_Bailly\\src\\fr\\iut\\obj2json\\MockObj.txt")));
        System.out.println(res);
        Assert.assertEquals(res, expected);
	}

}
