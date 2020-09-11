#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :run_miner.py
@Description  :Type whatever you want
@Datatime     :2020/08/29 20:25:46
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import sys
import sh.shell as shell

argv = sys.argv
if len(argv) > 1:
    shell.runMiner(argv[1:])
