// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chronicle.proto

package org.exodus.localfullnode2.chronicle.rpc;

public final class Chronicle {
  private Chronicle() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017chronicle.proto\022\003rpc\032\014common.proto\032\033go" +
      "ogle/protobuf/empty.proto2\277\002\n\032ChronicleD" +
      "umperRestorerRPC\022<\n\020GetMessageHashes\022\026.g" +
      "oogle.protobuf.Empty\032\020.rpc.StringArray\022?" +
      "\n\023GetSysMessageHashes\022\026.google.protobuf." +
      "Empty\032\020.rpc.StringArray\0227\n\022GetMessageStr" +
      "eamBy\022\020.rpc.StringArray\032\017.rpc.ByteStream" +
      "\0222\n\007Persist\022\017.rpc.ByteStream\032\026.google.pr" +
      "otobuf.Empty\0225\n\nPersistSys\022\017.rpc.ByteStr" +
      "eam\032\026.google.protobuf.Empty2\321\002\n Chronicl" +
      "eDumperRestorerStreamRPC\022>\n\020GetMessageHa" +
      "shes\022\026.google.protobuf.Empty\032\020.rpc.Strin" +
      "gArray0\001\022A\n\023GetSysMessageHashes\022\026.google" +
      ".protobuf.Empty\032\020.rpc.StringArray0\001\022;\n\022G" +
      "etMessageStreamBy\022\020.rpc.StringArray\032\017.rp" +
      "c.ByteStream(\0010\001\0224\n\007Persist\022\017.rpc.ByteSt" +
      "ream\032\026.google.protobuf.Empty(\001\0227\n\nPersis" +
      "tSys\022\017.rpc.ByteStream\032\026.google.protobuf." +
      "Empty(\001B)\n%org.exodus.localfullnode2.chron" +
      "icle.rpcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          org.exodus.localfullnode2.chronicle.rpc.Common.getDescriptor(),
          com.google.protobuf.EmptyProto.getDescriptor(),
        });
    org.exodus.localfullnode2.chronicle.rpc.Common.getDescriptor();
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}