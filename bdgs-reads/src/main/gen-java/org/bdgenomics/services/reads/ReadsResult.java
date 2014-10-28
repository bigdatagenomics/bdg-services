/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.bdgenomics.services.reads;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.EncodingUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;

public class ReadsResult implements org.apache.thrift.TBase<ReadsResult, ReadsResult._Fields>, java.io.Serializable, Cloneable, Comparable<ReadsResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ReadsResult");

  private static final org.apache.thrift.protocol.TField NEXT_START_FIELD_DESC = new org.apache.thrift.protocol.TField("next_start", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField READS_FIELD_DESC = new org.apache.thrift.protocol.TField("reads", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ReadsResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ReadsResultTupleSchemeFactory());
  }

  public int next_start; // optional
  public List<Read> reads; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NEXT_START((short)1, "next_start"),
    READS((short)2, "reads");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NEXT_START
          return NEXT_START;
        case 2: // READS
          return READS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __NEXT_START_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.NEXT_START};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NEXT_START, new org.apache.thrift.meta_data.FieldMetaData("next_start", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.READS, new org.apache.thrift.meta_data.FieldMetaData("reads", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Read.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ReadsResult.class, metaDataMap);
  }

  public ReadsResult() {
  }

  public ReadsResult(
    List<Read> reads)
  {
    this();
    this.reads = reads;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ReadsResult(ReadsResult other) {
    __isset_bitfield = other.__isset_bitfield;
    this.next_start = other.next_start;
    if (other.isSetReads()) {
      List<Read> __this__reads = new ArrayList<Read>(other.reads.size());
      for (Read other_element : other.reads) {
        __this__reads.add(new Read(other_element));
      }
      this.reads = __this__reads;
    }
  }

  public ReadsResult deepCopy() {
    return new ReadsResult(this);
  }

  @Override
  public void clear() {
    setNext_startIsSet(false);
    this.next_start = 0;
    this.reads = null;
  }

  public int getNext_start() {
    return this.next_start;
  }

  public ReadsResult setNext_start(int next_start) {
    this.next_start = next_start;
    setNext_startIsSet(true);
    return this;
  }

  public void unsetNext_start() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __NEXT_START_ISSET_ID);
  }

  /** Returns true if field next_start is set (has been assigned a value) and false otherwise */
  public boolean isSetNext_start() {
    return EncodingUtils.testBit(__isset_bitfield, __NEXT_START_ISSET_ID);
  }

  public void setNext_startIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __NEXT_START_ISSET_ID, value);
  }

  public int getReadsSize() {
    return (this.reads == null) ? 0 : this.reads.size();
  }

  public java.util.Iterator<Read> getReadsIterator() {
    return (this.reads == null) ? null : this.reads.iterator();
  }

  public void addToReads(Read elem) {
    if (this.reads == null) {
      this.reads = new ArrayList<Read>();
    }
    this.reads.add(elem);
  }

  public List<Read> getReads() {
    return this.reads;
  }

  public ReadsResult setReads(List<Read> reads) {
    this.reads = reads;
    return this;
  }

  public void unsetReads() {
    this.reads = null;
  }

  /** Returns true if field reads is set (has been assigned a value) and false otherwise */
  public boolean isSetReads() {
    return this.reads != null;
  }

  public void setReadsIsSet(boolean value) {
    if (!value) {
      this.reads = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case NEXT_START:
      if (value == null) {
        unsetNext_start();
      } else {
        setNext_start((Integer)value);
      }
      break;

    case READS:
      if (value == null) {
        unsetReads();
      } else {
        setReads((List<Read>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case NEXT_START:
      return Integer.valueOf(getNext_start());

    case READS:
      return getReads();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case NEXT_START:
      return isSetNext_start();
    case READS:
      return isSetReads();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ReadsResult)
      return this.equals((ReadsResult)that);
    return false;
  }

  public boolean equals(ReadsResult that) {
    if (that == null)
      return false;

    boolean this_present_next_start = true && this.isSetNext_start();
    boolean that_present_next_start = true && that.isSetNext_start();
    if (this_present_next_start || that_present_next_start) {
      if (!(this_present_next_start && that_present_next_start))
        return false;
      if (this.next_start != that.next_start)
        return false;
    }

    boolean this_present_reads = true && this.isSetReads();
    boolean that_present_reads = true && that.isSetReads();
    if (this_present_reads || that_present_reads) {
      if (!(this_present_reads && that_present_reads))
        return false;
      if (!this.reads.equals(that.reads))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(ReadsResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetNext_start()).compareTo(other.isSetNext_start());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNext_start()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.next_start, other.next_start);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetReads()).compareTo(other.isSetReads());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReads()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.reads, other.reads);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ReadsResult(");
    boolean first = true;

    if (isSetNext_start()) {
      sb.append("next_start:");
      sb.append(this.next_start);
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("reads:");
    if (this.reads == null) {
      sb.append("null");
    } else {
      sb.append(this.reads);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (reads == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'reads' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ReadsResultStandardSchemeFactory implements SchemeFactory {
    public ReadsResultStandardScheme getScheme() {
      return new ReadsResultStandardScheme();
    }
  }

  private static class ReadsResultStandardScheme extends StandardScheme<ReadsResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ReadsResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NEXT_START
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.next_start = iprot.readI32();
              struct.setNext_startIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // READS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.reads = new ArrayList<Read>(_list16.size);
                for (int _i17 = 0; _i17 < _list16.size; ++_i17)
                {
                  Read _elem18;
                  _elem18 = new Read();
                  _elem18.read(iprot);
                  struct.reads.add(_elem18);
                }
                iprot.readListEnd();
              }
              struct.setReadsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ReadsResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetNext_start()) {
        oprot.writeFieldBegin(NEXT_START_FIELD_DESC);
        oprot.writeI32(struct.next_start);
        oprot.writeFieldEnd();
      }
      if (struct.reads != null) {
        oprot.writeFieldBegin(READS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.reads.size()));
          for (Read _iter19 : struct.reads)
          {
            _iter19.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ReadsResultTupleSchemeFactory implements SchemeFactory {
    public ReadsResultTupleScheme getScheme() {
      return new ReadsResultTupleScheme();
    }
  }

  private static class ReadsResultTupleScheme extends TupleScheme<ReadsResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ReadsResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      {
        oprot.writeI32(struct.reads.size());
        for (Read _iter20 : struct.reads)
        {
          _iter20.write(oprot);
        }
      }
      BitSet optionals = new BitSet();
      if (struct.isSetNext_start()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetNext_start()) {
        oprot.writeI32(struct.next_start);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ReadsResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list21 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.reads = new ArrayList<Read>(_list21.size);
        for (int _i22 = 0; _i22 < _list21.size; ++_i22)
        {
          Read _elem23;
          _elem23 = new Read();
          _elem23.read(iprot);
          struct.reads.add(_elem23);
        }
      }
      struct.setReadsIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.next_start = iprot.readI32();
        struct.setNext_startIsSet(true);
      }
    }
  }

}

