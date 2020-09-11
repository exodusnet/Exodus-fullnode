# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :mining_plot.py
@Description  :build plot content 
@Datatime     :2020/08/31 04:13:48
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import struct
import util.shabal256 as shabal256
import util.ba as ba


class MiningPlot():
    HASH_SIZE = 32
    HASHES_PER_SCOOP = 2
    SCOOP_SIZE = HASHES_PER_SCOOP * HASH_SIZE
    SCOOPS_PER_PLOT = 4096
    PLOT_SIZE = SCOOPS_PER_PLOT * SCOOP_SIZE

    HASH_CAP = 4096

    def __init__(self, address, nonce):
        self.data = bytearray(MiningPlot.PLOT_SIZE)
        sz = struct.calcsize('qq')

        base = struct.pack('qq', int(address), int(nonce))
        gendata = bytearray(MiningPlot.PLOT_SIZE + sz)
        ba.copy(base, 0, gendata, MiningPlot.PLOT_SIZE, sz)

        i = MiningPlot.PLOT_SIZE
        while(i > 0):
            len = MiningPlot.HASH_CAP

            sliced = ba.slice(gendata, i, len)
            hashed = shabal256.digestFromBytearray(sliced)
            hashedHex = bytearray.fromhex(hashed)
            ba.copy(hashedHex, 0, gendata, i - MiningPlot.HASH_SIZE, MiningPlot.HASH_SIZE)

            i = i - MiningPlot.HASH_SIZE

        finalHash = shabal256.digestFromBytearray(gendata)
        finalHashHex = bytearray.fromhex(finalHash)
        for index in range(0, MiningPlot.PLOT_SIZE):
            self.data[index] = gendata[index] ^ finalHashHex[index % MiningPlot.HASH_SIZE]

    def getScoop(self, iPos):
        pos = int(iPos)
        return ba.slice(self.data, pos * MiningPlot.SCOOP_SIZE, (pos + 1) * MiningPlot.SCOOP_SIZE)
