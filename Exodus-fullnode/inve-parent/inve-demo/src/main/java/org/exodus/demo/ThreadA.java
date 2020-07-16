package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import java.awt.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.exodus.cluster.Member;

public class ThreadA extends Thread{
//	Signal signal;
	ThreadB threadB;
	ThreadC threadC;
	public ThreadA( ThreadB threadB, ThreadC threadC) {
//		this.signal = signal;
		this.threadB = threadB;
		this.threadC = threadC;
	}
	@Override
	public void run() {
		while(true) {
			 
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
//				List members = (List) threadB.node01.members().stream().collect(Collectors.toList());
				for(Member m: new ArrayList<Member>(threadB.node01.members())) {
					System.out.println(m.address().toString());
				}
				System.out.println("----------------");
//				System.out.println("node02 (" + threadB.node01.address() + ") cluster: "
//				        + threadB.node01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//				
			 
		}
	}

	public static void main(String[] args) {
		 Signal signal = new Signal();
		 ThreadB threadB = new ThreadB();
		 ThreadC threadc = new ThreadC();
		 ThreadA threadA = new ThreadA(threadB, threadc);
		 threadB.start();
		 threadc.start();
		 threadA.start();
		 
		 

	}

}
