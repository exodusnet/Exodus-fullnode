package org.exodus.localfullnode2.firstseq;

public interface FirstSeqsDependent {
	int getShardCount();

	long[][] getLastSeqs();

	int getnValue();

	String getDbId();
}
