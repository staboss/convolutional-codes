# Implementation of (non)systematic coding of convolutional codes and Viterbi decoding
### How to build:
`mvn package`
### How to run:
`java -jar ConvolutionalCoding-1.0.jar [-u|-s] [-r|-g] G1 G2`
    * `-s` : systematic coding
    * `-u` : unsystematic coding
    * `-g` : generating sequence
    * `-r` : read sequence from console
    * ` G` : the polynomial (from 1 to x^6)
### For example:
`java -jar ConvolutionalCoding-1.0.jar -u -g 1+x+x^4 1+x^2+x^3+x^4`