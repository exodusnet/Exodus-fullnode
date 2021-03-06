// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.7.1
//
// <auto-generated>
//
// Generated from file `Hashnet1.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package org.exodus.localfullnode2.rpc;

public interface Local2localPrx extends com.zeroc.Ice.ObjectPrx
{
    default GossipObj gossipMyMaxSeqList4Consensus(String pubkey, String sig, String snapVersion, String snapHash, long[] seqs)
    {
        return gossipMyMaxSeqList4Consensus(pubkey, sig, snapVersion, snapHash, seqs, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default GossipObj gossipMyMaxSeqList4Consensus(String pubkey, String sig, String snapVersion, String snapHash, long[] seqs, java.util.Map<String, String> context)
    {
        return _iceI_gossipMyMaxSeqList4ConsensusAsync(pubkey, sig, snapVersion, snapHash, seqs, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<GossipObj> gossipMyMaxSeqList4ConsensusAsync(String pubkey, String sig, String snapVersion, String snapHash, long[] seqs)
    {
        return _iceI_gossipMyMaxSeqList4ConsensusAsync(pubkey, sig, snapVersion, snapHash, seqs, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<GossipObj> gossipMyMaxSeqList4ConsensusAsync(String pubkey, String sig, String snapVersion, String snapHash, long[] seqs, java.util.Map<String, String> context)
    {
        return _iceI_gossipMyMaxSeqList4ConsensusAsync(pubkey, sig, snapVersion, snapHash, seqs, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<GossipObj> _iceI_gossipMyMaxSeqList4ConsensusAsync(String iceP_pubkey, String iceP_sig, String iceP_snapVersion, String iceP_snapHash, long[] iceP_seqs, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<GossipObj> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossipMyMaxSeqList4Consensus", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeString(iceP_snapVersion);
                     ostr.writeString(iceP_snapHash);
                     ostr.writeLongSeq(iceP_seqs);
                 }, istr -> {
                     GossipObj ret;
                     ret = GossipObj.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    default GossipObj gossipMyMaxSeqList4Sync(String pubkey, String sig, int otherShardId, String snapVersion, String snapHash, long[] seqs)
    {
        return gossipMyMaxSeqList4Sync(pubkey, sig, otherShardId, snapVersion, snapHash, seqs, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default GossipObj gossipMyMaxSeqList4Sync(String pubkey, String sig, int otherShardId, String snapVersion, String snapHash, long[] seqs, java.util.Map<String, String> context)
    {
        return _iceI_gossipMyMaxSeqList4SyncAsync(pubkey, sig, otherShardId, snapVersion, snapHash, seqs, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<GossipObj> gossipMyMaxSeqList4SyncAsync(String pubkey, String sig, int otherShardId, String snapVersion, String snapHash, long[] seqs)
    {
        return _iceI_gossipMyMaxSeqList4SyncAsync(pubkey, sig, otherShardId, snapVersion, snapHash, seqs, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<GossipObj> gossipMyMaxSeqList4SyncAsync(String pubkey, String sig, int otherShardId, String snapVersion, String snapHash, long[] seqs, java.util.Map<String, String> context)
    {
        return _iceI_gossipMyMaxSeqList4SyncAsync(pubkey, sig, otherShardId, snapVersion, snapHash, seqs, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<GossipObj> _iceI_gossipMyMaxSeqList4SyncAsync(String iceP_pubkey, String iceP_sig, int iceP_otherShardId, String iceP_snapVersion, String iceP_snapHash, long[] iceP_seqs, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<GossipObj> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossipMyMaxSeqList4Sync", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeInt(iceP_otherShardId);
                     ostr.writeString(iceP_snapVersion);
                     ostr.writeString(iceP_snapHash);
                     ostr.writeLongSeq(iceP_seqs);
                 }, istr -> {
                     GossipObj ret;
                     ret = GossipObj.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    default SnapObj gossipMySnapVersion4Snap(String pubkey, String sig, String hash, String transCount)
    {
        return gossipMySnapVersion4Snap(pubkey, sig, hash, transCount, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default SnapObj gossipMySnapVersion4Snap(String pubkey, String sig, String hash, String transCount, java.util.Map<String, String> context)
    {
        return _iceI_gossipMySnapVersion4SnapAsync(pubkey, sig, hash, transCount, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<SnapObj> gossipMySnapVersion4SnapAsync(String pubkey, String sig, String hash, String transCount)
    {
        return _iceI_gossipMySnapVersion4SnapAsync(pubkey, sig, hash, transCount, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<SnapObj> gossipMySnapVersion4SnapAsync(String pubkey, String sig, String hash, String transCount, java.util.Map<String, String> context)
    {
        return _iceI_gossipMySnapVersion4SnapAsync(pubkey, sig, hash, transCount, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<SnapObj> _iceI_gossipMySnapVersion4SnapAsync(String iceP_pubkey, String iceP_sig, String iceP_hash, String iceP_transCount, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<SnapObj> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossipMySnapVersion4Snap", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeString(iceP_hash);
                     ostr.writeString(iceP_transCount);
                 }, istr -> {
                     SnapObj ret;
                     ret = SnapObj.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    default AppointEvent gossip4AppointEvent(String pubkey, String sig, int shardId, long creatorId, long creatorSeq)
    {
        return gossip4AppointEvent(pubkey, sig, shardId, creatorId, creatorSeq, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default AppointEvent gossip4AppointEvent(String pubkey, String sig, int shardId, long creatorId, long creatorSeq, java.util.Map<String, String> context)
    {
        return _iceI_gossip4AppointEventAsync(pubkey, sig, shardId, creatorId, creatorSeq, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<AppointEvent> gossip4AppointEventAsync(String pubkey, String sig, int shardId, long creatorId, long creatorSeq)
    {
        return _iceI_gossip4AppointEventAsync(pubkey, sig, shardId, creatorId, creatorSeq, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<AppointEvent> gossip4AppointEventAsync(String pubkey, String sig, int shardId, long creatorId, long creatorSeq, java.util.Map<String, String> context)
    {
        return _iceI_gossip4AppointEventAsync(pubkey, sig, shardId, creatorId, creatorSeq, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<AppointEvent> _iceI_gossip4AppointEventAsync(String iceP_pubkey, String iceP_sig, int iceP_shardId, long iceP_creatorId, long iceP_creatorSeq, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<AppointEvent> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossip4AppointEvent", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeInt(iceP_shardId);
                     ostr.writeLong(iceP_creatorId);
                     ostr.writeLong(iceP_creatorSeq);
                 }, istr -> {
                     AppointEvent ret;
                     ret = AppointEvent.ice_read(istr);
                     return ret;
                 });
        return f;
    }

    default boolean gossipReport4split(String pubkey, String sig, String data, int shardId, String event)
    {
        return gossipReport4split(pubkey, sig, data, shardId, event, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default boolean gossipReport4split(String pubkey, String sig, String data, int shardId, String event, java.util.Map<String, String> context)
    {
        return _iceI_gossipReport4splitAsync(pubkey, sig, data, shardId, event, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> gossipReport4splitAsync(String pubkey, String sig, String data, int shardId, String event)
    {
        return _iceI_gossipReport4splitAsync(pubkey, sig, data, shardId, event, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> gossipReport4splitAsync(String pubkey, String sig, String data, int shardId, String event, java.util.Map<String, String> context)
    {
        return _iceI_gossipReport4splitAsync(pubkey, sig, data, shardId, event, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> _iceI_gossipReport4splitAsync(String iceP_pubkey, String iceP_sig, String iceP_data, int iceP_shardId, String iceP_event, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossipReport4split", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeString(iceP_data);
                     ostr.writeInt(iceP_shardId);
                     ostr.writeString(iceP_event);
                 }, istr -> {
                     boolean ret;
                     ret = istr.readBool();
                     return ret;
                 });
        return f;
    }

    default boolean gossip4SplitDel(String pubkey, String sig, String data, int shardId, long creatorId, long creatorSeq, String eventHash, boolean isNeedGossip2Center)
    {
        return gossip4SplitDel(pubkey, sig, data, shardId, creatorId, creatorSeq, eventHash, isNeedGossip2Center, com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default boolean gossip4SplitDel(String pubkey, String sig, String data, int shardId, long creatorId, long creatorSeq, String eventHash, boolean isNeedGossip2Center, java.util.Map<String, String> context)
    {
        return _iceI_gossip4SplitDelAsync(pubkey, sig, data, shardId, creatorId, creatorSeq, eventHash, isNeedGossip2Center, context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> gossip4SplitDelAsync(String pubkey, String sig, String data, int shardId, long creatorId, long creatorSeq, String eventHash, boolean isNeedGossip2Center)
    {
        return _iceI_gossip4SplitDelAsync(pubkey, sig, data, shardId, creatorId, creatorSeq, eventHash, isNeedGossip2Center, com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<java.lang.Boolean> gossip4SplitDelAsync(String pubkey, String sig, String data, int shardId, long creatorId, long creatorSeq, String eventHash, boolean isNeedGossip2Center, java.util.Map<String, String> context)
    {
        return _iceI_gossip4SplitDelAsync(pubkey, sig, data, shardId, creatorId, creatorSeq, eventHash, isNeedGossip2Center, context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> _iceI_gossip4SplitDelAsync(String iceP_pubkey, String iceP_sig, String iceP_data, int iceP_shardId, long iceP_creatorId, long iceP_creatorSeq, String iceP_eventHash, boolean iceP_isNeedGossip2Center, java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<java.lang.Boolean> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "gossip4SplitDel", null, sync, null);
        f.invoke(true, context, null, ostr -> {
                     ostr.writeString(iceP_pubkey);
                     ostr.writeString(iceP_sig);
                     ostr.writeString(iceP_data);
                     ostr.writeInt(iceP_shardId);
                     ostr.writeLong(iceP_creatorId);
                     ostr.writeLong(iceP_creatorSeq);
                     ostr.writeString(iceP_eventHash);
                     ostr.writeBool(iceP_isNeedGossip2Center);
                 }, istr -> {
                     boolean ret;
                     ret = istr.readBool();
                     return ret;
                 });
        return f;
    }

    default long[] getHeight()
    {
        return getHeight(com.zeroc.Ice.ObjectPrx.noExplicitContext);
    }

    default long[] getHeight(java.util.Map<String, String> context)
    {
        return _iceI_getHeightAsync(context, true).waitForResponse();
    }

    default java.util.concurrent.CompletableFuture<long[]> getHeightAsync()
    {
        return _iceI_getHeightAsync(com.zeroc.Ice.ObjectPrx.noExplicitContext, false);
    }

    default java.util.concurrent.CompletableFuture<long[]> getHeightAsync(java.util.Map<String, String> context)
    {
        return _iceI_getHeightAsync(context, false);
    }

    default com.zeroc.IceInternal.OutgoingAsync<long[]> _iceI_getHeightAsync(java.util.Map<String, String> context, boolean sync)
    {
        com.zeroc.IceInternal.OutgoingAsync<long[]> f = new com.zeroc.IceInternal.OutgoingAsync<>(this, "getHeight", null, sync, null);
        f.invoke(true, context, null, null, istr -> {
                     long[] ret;
                     ret = istr.readLongSeq();
                     return ret;
                 });
        return f;
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static Local2localPrx checkedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, ice_staticId(), Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Contacts the remote server to verify that the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static Local2localPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, context, ice_staticId(), Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static Local2localPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, ice_staticId(), Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Contacts the remote server to verify that a facet of the object implements this type.
     * Raises a local exception if a communication error occurs.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @param context The Context map to send with the invocation.
     * @return A proxy for this type, or null if the object does not support this type.
     **/
    static Local2localPrx checkedCast(com.zeroc.Ice.ObjectPrx obj, String facet, java.util.Map<String, String> context)
    {
        return com.zeroc.Ice.ObjectPrx._checkedCast(obj, facet, context, ice_staticId(), Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @return A proxy for this type.
     **/
    static Local2localPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Downcasts the given proxy to this type without contacting the remote server.
     * @param obj The untyped proxy.
     * @param facet The name of the desired facet.
     * @return A proxy for this type.
     **/
    static Local2localPrx uncheckedCast(com.zeroc.Ice.ObjectPrx obj, String facet)
    {
        return com.zeroc.Ice.ObjectPrx._uncheckedCast(obj, facet, Local2localPrx.class, _Local2localPrxI.class);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the per-proxy context.
     * @param newContext The context for the new proxy.
     * @return A proxy with the specified per-proxy context.
     **/
    @Override
    default Local2localPrx ice_context(java.util.Map<String, String> newContext)
    {
        return (Local2localPrx)_ice_context(newContext);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the adapter ID.
     * @param newAdapterId The adapter ID for the new proxy.
     * @return A proxy with the specified adapter ID.
     **/
    @Override
    default Local2localPrx ice_adapterId(String newAdapterId)
    {
        return (Local2localPrx)_ice_adapterId(newAdapterId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoints.
     * @param newEndpoints The endpoints for the new proxy.
     * @return A proxy with the specified endpoints.
     **/
    @Override
    default Local2localPrx ice_endpoints(com.zeroc.Ice.Endpoint[] newEndpoints)
    {
        return (Local2localPrx)_ice_endpoints(newEndpoints);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator cache timeout.
     * @param newTimeout The new locator cache timeout (in seconds).
     * @return A proxy with the specified locator cache timeout.
     **/
    @Override
    default Local2localPrx ice_locatorCacheTimeout(int newTimeout)
    {
        return (Local2localPrx)_ice_locatorCacheTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the invocation timeout.
     * @param newTimeout The new invocation timeout (in seconds).
     * @return A proxy with the specified invocation timeout.
     **/
    @Override
    default Local2localPrx ice_invocationTimeout(int newTimeout)
    {
        return (Local2localPrx)_ice_invocationTimeout(newTimeout);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for connection caching.
     * @param newCache <code>true</code> if the new proxy should cache connections; <code>false</code> otherwise.
     * @return A proxy with the specified caching policy.
     **/
    @Override
    default Local2localPrx ice_connectionCached(boolean newCache)
    {
        return (Local2localPrx)_ice_connectionCached(newCache);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the endpoint selection policy.
     * @param newType The new endpoint selection policy.
     * @return A proxy with the specified endpoint selection policy.
     **/
    @Override
    default Local2localPrx ice_endpointSelection(com.zeroc.Ice.EndpointSelectionType newType)
    {
        return (Local2localPrx)_ice_endpointSelection(newType);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for how it selects endpoints.
     * @param b If <code>b</code> is <code>true</code>, only endpoints that use a secure transport are
     * used by the new proxy. If <code>b</code> is false, the returned proxy uses both secure and
     * insecure endpoints.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default Local2localPrx ice_secure(boolean b)
    {
        return (Local2localPrx)_ice_secure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the encoding used to marshal parameters.
     * @param e The encoding version to use to marshal request parameters.
     * @return A proxy with the specified encoding version.
     **/
    @Override
    default Local2localPrx ice_encodingVersion(com.zeroc.Ice.EncodingVersion e)
    {
        return (Local2localPrx)_ice_encodingVersion(e);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its endpoint selection policy.
     * @param b If <code>b</code> is <code>true</code>, the new proxy will use secure endpoints for invocations
     * and only use insecure endpoints if an invocation cannot be made via secure endpoints. If <code>b</code> is
     * <code>false</code>, the proxy prefers insecure endpoints to secure ones.
     * @return A proxy with the specified selection policy.
     **/
    @Override
    default Local2localPrx ice_preferSecure(boolean b)
    {
        return (Local2localPrx)_ice_preferSecure(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the router.
     * @param router The router for the new proxy.
     * @return A proxy with the specified router.
     **/
    @Override
    default Local2localPrx ice_router(com.zeroc.Ice.RouterPrx router)
    {
        return (Local2localPrx)_ice_router(router);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for the locator.
     * @param locator The locator for the new proxy.
     * @return A proxy with the specified locator.
     **/
    @Override
    default Local2localPrx ice_locator(com.zeroc.Ice.LocatorPrx locator)
    {
        return (Local2localPrx)_ice_locator(locator);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for collocation optimization.
     * @param b <code>true</code> if the new proxy enables collocation optimization; <code>false</code> otherwise.
     * @return A proxy with the specified collocation optimization.
     **/
    @Override
    default Local2localPrx ice_collocationOptimized(boolean b)
    {
        return (Local2localPrx)_ice_collocationOptimized(b);
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses twoway invocations.
     * @return A proxy that uses twoway invocations.
     **/
    @Override
    default Local2localPrx ice_twoway()
    {
        return (Local2localPrx)_ice_twoway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses oneway invocations.
     * @return A proxy that uses oneway invocations.
     **/
    @Override
    default Local2localPrx ice_oneway()
    {
        return (Local2localPrx)_ice_oneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch oneway invocations.
     * @return A proxy that uses batch oneway invocations.
     **/
    @Override
    default Local2localPrx ice_batchOneway()
    {
        return (Local2localPrx)_ice_batchOneway();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses datagram invocations.
     * @return A proxy that uses datagram invocations.
     **/
    @Override
    default Local2localPrx ice_datagram()
    {
        return (Local2localPrx)_ice_datagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, but uses batch datagram invocations.
     * @return A proxy that uses batch datagram invocations.
     **/
    @Override
    default Local2localPrx ice_batchDatagram()
    {
        return (Local2localPrx)_ice_batchDatagram();
    }

    /**
     * Returns a proxy that is identical to this proxy, except for compression.
     * @param co <code>true</code> enables compression for the new proxy; <code>false</code> disables compression.
     * @return A proxy with the specified compression setting.
     **/
    @Override
    default Local2localPrx ice_compress(boolean co)
    {
        return (Local2localPrx)_ice_compress(co);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection timeout setting.
     * @param t The connection timeout for the proxy in milliseconds.
     * @return A proxy with the specified timeout.
     **/
    @Override
    default Local2localPrx ice_timeout(int t)
    {
        return (Local2localPrx)_ice_timeout(t);
    }

    /**
     * Returns a proxy that is identical to this proxy, except for its connection ID.
     * @param connectionId The connection ID for the new proxy. An empty string removes the connection ID.
     * @return A proxy with the specified connection ID.
     **/
    @Override
    default Local2localPrx ice_connectionId(String connectionId)
    {
        return (Local2localPrx)_ice_connectionId(connectionId);
    }

    /**
     * Returns a proxy that is identical to this proxy, except it's a fixed proxy bound
     * the given connection.@param connection The fixed proxy connection.
     * @return A fixed proxy bound to the given connection.
     **/
    @Override
    default Local2localPrx ice_fixed(com.zeroc.Ice.Connection connection)
    {
        return (Local2localPrx)_ice_fixed(connection);
    }

    static String ice_staticId()
    {
        return "::rpc::Local2local";
    }
}
