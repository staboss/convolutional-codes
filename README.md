# Implementation of the data transmission BSC model 
## Build project
```
mvn package
```

## Usage
```
---------------------------------------------------------------
usage : java -jar convolutional-codes.jar [-u|-s] [-r|-g] G1 G2
---------------------------------------------------------------
	-u : unsystematic coding
	-s : systematic coding
	-g : generating sequence
	-r : read sequence from console
	 K : the length of information sequence [-g flag]
	 P : the probability to invert bit [-g flag]
	 I : the number of iterations [-g flag]
	 G : the polynomial (from 1 to x^6)
---------------------------------------------------------------
```

## Example
```
java -jar convolutional-codes.jar -u -g 1+x+x^4 1+x^2+x^3+x^4
```

## Binary symmetric channel (BSC) model
[Click](https://github.com/staboss/convolutional-codes/tree/master/examples/Model.pdf) for detail view.

## Frame error rate (FER) test
![BER test](https://github.com/staboss/convolutional-codes/blob/master/media/ERROR_RATE/ERROR_RATE_X_4/FER_4.png)

## Bit error rate (BER) test
![BER test](https://github.com/staboss/convolutional-codes/blob/master/media/ERROR_RATE/ERROR_RATE_X_4/BER_4.png)

## TODO
- [ ] Translate all comments in the code into English

## License & copyright
Licensed under the [MIT-License](LICENSE.md).
