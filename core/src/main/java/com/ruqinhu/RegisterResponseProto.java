// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: RegisterResponse.proto

package com.ruqinhu;

public final class RegisterResponseProto {
  private RegisterResponseProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_RegisterResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_RegisterResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_RegisterResponse_ValueEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_RegisterResponse_ValueEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026RegisterResponse.proto\"\242\001\n\020RegisterRes" +
      "ponse\022\017\n\007success\030\001 \001(\010\022+\n\005value\030\002 \003(\0132\034." +
      "RegisterResponse.ValueEntry\022\020\n\010redirect\030" +
      "\003 \001(\t\022\020\n\010errorMsg\030\004 \001(\t\032,\n\nValueEntry\022\013\n" +
      "\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:\0028\001B&\n\013com.ruq" +
      "inhuB\025RegisterResponseProtoP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_RegisterResponse_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_RegisterResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_RegisterResponse_descriptor,
        new java.lang.String[] { "Success", "Value", "Redirect", "ErrorMsg", });
    internal_static_RegisterResponse_ValueEntry_descriptor =
      internal_static_RegisterResponse_descriptor.getNestedTypes().get(0);
    internal_static_RegisterResponse_ValueEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_RegisterResponse_ValueEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
