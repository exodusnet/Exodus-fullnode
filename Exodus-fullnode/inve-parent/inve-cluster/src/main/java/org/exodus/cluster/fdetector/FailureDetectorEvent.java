package org.exodus.cluster.fdetector;

import javax.annotation.concurrent.Immutable;

import org.exodus.cluster.Member;
import org.exodus.cluster.membership.MemberStatus;

/**
 * CLass contains result of ping check. */
@Immutable
public final class FailureDetectorEvent {

  private final Member member;
  private final MemberStatus status;

  FailureDetectorEvent(Member member, MemberStatus status) {
    this.member = member;
    this.status = status;
  }

  public Member member() {
    return member;
  }

  public MemberStatus status() {
    return status;
  }

  @Override
  public String toString() {
    return "FailureDetectorEvent{member=" + member + ", status=" + status + '}';
  }
}
