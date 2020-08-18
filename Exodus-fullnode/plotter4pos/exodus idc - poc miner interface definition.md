# exodus idc - pocminer interface definition

## Revision history

| date      | version | description | modifier |
| --------- | ------- | ----------- | -------- |
| 8/16/2020 | 0.1     |             | Francis  |


## Command line format
java -jar plotter4pos-0.0.1-SNAPSHOT.jar [generate [...]|dumpwords|mine]




## Command line declaration

### generate plot file to occupy disk space, which is used to claim your contribution later
```
java -jar plotter4pos-0.0.1-SNAPSHOT.jar generate --plots [size]
```

** the size of occupied disk space = 256k multiply [size]^2 **

for example:
occupy disk space of 100m
```
java -jar plotter4pos-0.0.1-SNAPSHOT.jar generate --plots 20
```

occupy disk space of 25m
```
java -jar plotter4pos-0.0.1-SNAPSHOT.jar generate --plots 10
```


### know wallet words,which is very useful while it's a new-generated wallet
```
java -jar plotter4pos-0.0.1-SNAPSHOT.jar dumpwords
```

### claim router's contribution which is related to your plot file size.The more size your plot file is,the more possibility the router wallet is able to gain to get reward.
```
java -jar plotter4pos-0.0.1-SNAPSHOT.jar mine
```

