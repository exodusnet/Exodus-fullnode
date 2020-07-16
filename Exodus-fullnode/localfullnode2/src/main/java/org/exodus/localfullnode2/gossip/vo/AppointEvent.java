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
// Generated from file `Hashnet.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package org.exodus.localfullnode2.gossip.vo;

import org.exodus.localfullnode2.vo.Event;

@Deprecated
public class AppointEvent implements java.lang.Cloneable, java.io.Serializable {
	public String snapVersion;

	public Event event;

	public AppointEvent() {
		this.snapVersion = "";
		this.event = new Event();
	}

	public AppointEvent(String snapVersion, Event event) {
		this.snapVersion = snapVersion;
		this.event = event;
	}

	public boolean equals(java.lang.Object rhs) {
		if (this == rhs) {
			return true;
		}
		AppointEvent r = null;
		if (rhs instanceof AppointEvent) {
			r = (AppointEvent) rhs;
		}

		if (r != null) {
			if (this.snapVersion != r.snapVersion) {
				if (this.snapVersion == null || r.snapVersion == null || !this.snapVersion.equals(r.snapVersion)) {
					return false;
				}
			}
			if (this.event != r.event) {
				if (this.event == null || r.event == null || !this.event.equals(r.event)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public int hashCode() {
		int h_ = 5381;
		h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::one::inve::rpc::localfullnode::AppointEvent");
		h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, snapVersion);
		h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, event);
		return h_;
	}

	public AppointEvent clone() {
		AppointEvent c = null;
		try {
			c = (AppointEvent) super.clone();
		} catch (CloneNotSupportedException ex) {
			assert false; // impossible
		}
		return c;
	}

	public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr) {
		ostr.writeString(this.snapVersion);
		Event.ice_write(ostr, this.event);
	}

	public void ice_readMembers(com.zeroc.Ice.InputStream istr) {
		this.snapVersion = istr.readString();
		this.event = Event.ice_read(istr);
	}

	static public void ice_write(com.zeroc.Ice.OutputStream ostr, AppointEvent v) {
		if (v == null) {
			_nullMarshalValue.ice_writeMembers(ostr);
		} else {
			v.ice_writeMembers(ostr);
		}
	}

	static public AppointEvent ice_read(com.zeroc.Ice.InputStream istr) {
		AppointEvent v = new AppointEvent();
		v.ice_readMembers(istr);
		return v;
	}

	static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<AppointEvent> v) {
		if (v != null && v.isPresent()) {
			ice_write(ostr, tag, v.get());
		}
	}

	static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, AppointEvent v) {
		if (ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize)) {
			int pos = ostr.startSize();
			ice_write(ostr, v);
			ostr.endSize(pos);
		}
	}

	static public java.util.Optional<AppointEvent> ice_read(com.zeroc.Ice.InputStream istr, int tag) {
		if (istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize)) {
			istr.skip(4);
			return java.util.Optional.of(AppointEvent.ice_read(istr));
		} else {
			return java.util.Optional.empty();
		}
	}

	private static final AppointEvent _nullMarshalValue = new AppointEvent();

	public static final long serialVersionUID = -8457366623633222417L;
}
