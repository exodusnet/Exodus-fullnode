#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :run_miner.py
@Description  :python run_miner.py generate [plot number]
               python run_miner.py mine
@Datatime     :2020/08/29 20:25:46
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import sys
import sh.shell as shell

argv = sys.argv
if len(argv) > 1:
    shell.runMiner(argv)
