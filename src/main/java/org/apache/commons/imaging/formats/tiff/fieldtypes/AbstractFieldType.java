/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.imaging.formats.tiff.fieldtypes;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.TiffField;

/**
 * TIFF field types.
 */
public abstract class AbstractFieldType {
    public static AbstractFieldType x = null;

    private static AbstractFieldType createByteFieldTypeByName(String name) {
        if(Objects.equals(name, "BYTE")){
            x =  new FieldTypeByte(1, "Byte");
        }
        if(Objects.equals(name, "ASCII")){
            x =  new FieldTypeAscii(2, "ASCII");
        }
        if(Objects.equals(name, "Short")){
            x =  new FieldTypeShort(3, "Short");
        }
        if(Objects.equals(name, "Long")){
            x =  new FieldTypeLong(4, "Long");
        }
        if(Objects.equals(name, "Rational")){
            x =  new FieldTypeRational(5, "Rational");
        }
        if(Objects.equals(name, "SByte")){
            x =  new FieldTypeByte(6, "SByte");
        }
        if(Objects.equals(name, "Undefined")){
            x =  new FieldTypeByte(7, "Undefined");
        }
        if(Objects.equals(name, "SShort")){
            x =  new FieldTypeShort(8, "SShort");
        }
        if(Objects.equals(name, "SLong")){
            x =  new FieldTypeLong(9, "SLong");
        }
        if(Objects.equals(name, "SRational")){
            x =  new FieldTypeRational(10, "SRational");
        }
        if(Objects.equals(name, "Float")){
            x =  new FieldTypeFloat(11, "Float");
        }
        if(Objects.equals(name, "Double")){
            x =  new FieldTypeDouble(12, "Double");
        }
        if(Objects.equals(name, "IFD")){
            x =  new FieldTypeLong(13, "IFD");
        }
        if(Objects.equals(name, "Long8")){
            x =  new FieldTypeLong8(16, "Long8");
        }
        if(Objects.equals(name, "SLong8")){
            x =  new FieldTypeLong8(17, "SLong8");
        }
        if(Objects.equals(name, "IFD8")){
            x =  new FieldTypeLong8(18, "IFD8");
        }

        return x;
    }
    public static final AbstractFieldType BYTE = createByteFieldTypeByName("BYTE");
    public static final AbstractFieldType ASCII = createByteFieldTypeByName("ASCII");
    public static final AbstractFieldType SHORT = createByteFieldTypeByName("Short");
    public static final AbstractFieldType LONG = createByteFieldTypeByName("Long");
    public static final AbstractFieldType RATIONAL = createByteFieldTypeByName("Rational");
    public static final AbstractFieldType SBYTE = createByteFieldTypeByName("SByte");
    public static final AbstractFieldType UNDEFINED = createByteFieldTypeByName("Undefined");
    public static final AbstractFieldType SSHORT = createByteFieldTypeByName("SShort");
    public static final AbstractFieldType SLONG = createByteFieldTypeByName("SLong");
    public static final AbstractFieldType SRATIONAL = createByteFieldTypeByName("SRational");
    public static final AbstractFieldType FLOAT = createByteFieldTypeByName("Float");
    public static final AbstractFieldType DOUBLE = createByteFieldTypeByName("Double");
    public static final AbstractFieldType IFD = createByteFieldTypeByName("IFD");
    public static final AbstractFieldType LONG8 = createByteFieldTypeByName("Long8");
    public static final AbstractFieldType SLONG8 = createByteFieldTypeByName("SLong8");
    public static final AbstractFieldType IFD8 = createByteFieldTypeByName("IFD8");

    public static final List<AbstractFieldType> ANY = Collections.unmodifiableList(
            Arrays.asList(BYTE, ASCII, SHORT, LONG, RATIONAL, SBYTE, UNDEFINED, SSHORT, SLONG, SRATIONAL, FLOAT, DOUBLE, IFD, LONG8, SLONG8, IFD8));
    public static final List<AbstractFieldType> SHORT_OR_LONG = Collections.unmodifiableList(Arrays.asList(SHORT, LONG));
    public static final List<AbstractFieldType> SHORT_OR_RATIONAL = Collections.unmodifiableList(Arrays.asList(SHORT, RATIONAL));

    public static final List<AbstractFieldType> SHORT_OR_LONG_OR_RATIONAL = Collections.unmodifiableList(Arrays.asList(SHORT, LONG, RATIONAL));


    public static final List<AbstractFieldType> BYTE_OR_SHORT = Collections.unmodifiableList(Arrays.asList(SHORT, BYTE));

    public static final List<AbstractFieldType> LONG_OR_IFD = Collections.unmodifiableList(Arrays.asList(LONG, IFD));

    public static final List<AbstractFieldType> ASCII_OR_RATIONAL = Collections.unmodifiableList(Arrays.asList(ASCII, RATIONAL));

    public static final List<AbstractFieldType> ASCII_OR_BYTE = Collections.unmodifiableList(Arrays.asList(ASCII, BYTE));

    public static AbstractFieldType getFieldType(final int type) throws ImagingException {
        for (final AbstractFieldType abstractFieldType : ANY) {
            if (abstractFieldType.getType() == type) {
                return abstractFieldType;
            }
        }
        throw new ImagingException("Field type " + type + " is unsupported");
    }

    private final int type;

    private final String name;

    private final int elementSize;

    protected AbstractFieldType(final int type, final String name, final int elementSize) {
        this.type = type;
        this.name = name;
        this.elementSize = elementSize;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return elementSize;
    }

    public int getType() {
        return type;
    }

    public abstract Object getValue(TiffField entry);

    public abstract byte[] writeData(Object o, ByteOrder byteOrder) throws ImagingException;
}
