# Implementation of the data transmission model through BSC channel
### How to build:
`mvn package`
### How to run:
`java -jar ConvolutionalCoding-1.0.jar [-u|-s] [-r|-g] G1 G2`
- `-s` : systematic coding
- `-u` : unsystematic coding
- `-g` : generating sequence
- `-r` : read sequence from console
- `Gn` : the polynomial (from 1 to x^6)
    
### For example:
`java -jar ConvolutionalCoding-1.0.jar -u -g 1+x+x^4 1+x^2+x^3+x^4`

### Binary symmetric channel (BSC) model
![Model BSC](https://github.com/staboss/convolutional-codes/tree/master/examples/Model.png)

### Bit error rate (BER) test
![BER test](https://github.com/staboss/convolutional-codes/blob/master/src/main/resources/PlotBER_4.png)

### Frame error rate (FER) test
![BER test](https://github.com/staboss/convolutional-codes/blob/master/src/main/resources/PlotFER_4.png)

See [wiki](https://en.wikipedia.org/wiki/Viterbi_decoder) for more information.