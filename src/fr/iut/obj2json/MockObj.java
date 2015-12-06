package fr.iut.obj2json;

import java.util.Date;

public class MockObj {
    private boolean booleanValue;
    public int intValue;
    private byte byteValue;
    private char charValue;
    private short shortValue;
    private long longValue;
    private float floatValue;
    private double dblValue;
    private SubObj subObj = new SubObj();
    
    private boolean[] booleanValues = new boolean[] { true, false};
    private int[] intValues = new int[] { 1, 2 };
    private byte[] byteValues = new byte[] { (byte)1, (byte)2 };
    private char[] charValues = new char[] { 'a', 'b' };
    private short[] shortValues = new short[] { 1, 2 };
    private long[] longValues = new long[] { 1L, 2L };
    private float[] floatValues = new float[] { 1.0f, 2.0f };
    private double[] dblValues = new double[] { 1.0, 2.0 };

    private SubObj[] subObjs = new SubObj[] { new SubObj(), new SubObj(), null };

    private Date date;
    private String string = "test str";
    private Class<?> clss = StringBuilder.class;
}
