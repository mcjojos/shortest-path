Shortest path service
A program that can calculate the minimum cost between nodes.

## Synopsis
This program is calculating the minimum path between nodes. The nodes are connected with vertexes of different weight.
More specifically suppose you have a network of friends and want to ship a package to any one of your friends at the
optimal (lowest) cost. We will represent them in terms of abstract units called `HARD` which define the difficulty of
shipping a package. Each relationship is uni-directional, that is, the difficulty for shipping a package from you to
your friend on the moon may not be the same as the difficulty of shipping a package from your friend on the moon
to you. Also, your friends may have other friends so an optimal shipping cost may not always be direct shipping from
you to your friend, but perhaps through a number of your friends.

To each of your friends you can ship a package of certain weight and dimensions at a 
fixed cost. The formula for computing the shipment is the same for all of your 
friends in the network and is as follows:
```
{shipping cost}(EUR) = sqrt(sum(HARD)) * {normalized weight}(kg)
```
The cost is represented to the nearest two decimals (eg: 24,33 EUR). A normalized package 
weight is the greater value of an actual weight or a volumetric weight.

A `volumetric weight` (sometimes called dimensional weight) is a formula often applied by 
carriers to take into account volume as a function of weight. A light package that 
takes up a lot of space is just as difficult to ship as a small package that weighs a lot. 
The volumetric formula is defined as:
```
{Width x Length x Height}(cm) / 5000 = {Volumetric Weight}(kg) rounded up to the nearest 0,5kg
```
A practical example of this is [DHL](http://wap.dhl.com/serv/volweight.html). For example, 
a package of `width=26cm, length=10cm and height=11cm` that weighs `400 grams` would 
have a `normalized weight = 1kg` because volumetric weight defined as `2860/5000 = 0,572kg` 
rounded up to the `nearest 0,5kg` is `1kg`.

Suppose you have five friends in your network and this is how hard it is to ship a package 
to them:
```
You => Stefan:  100 HARD 
You => Amir:   1042 HARD 
You => Martin:  595 HARD 
You => Adam:     10 HARD 
You => Philipp: 128 HARD
```
You can assume `HARD` units are positive `integers`.

You also know how hard it is for some of your friends to ship a package between themselves, but 
since not all of your friends may be friends with each other you wouldn't know all shipping 
combinations. For example, you only know that:
```
Stefan => Amir: 850 HARD 
Stefan => Adam:  85 HARD 
Adam => Philipp:  7 HARD 
Adam => Martin: 400 HARD 
Diana => Amir:   57 HARD 
Diana => Martin:  3 HARD
```
_(notice that Diana is not your friend)_

Taking into account all of this information you know that you could ship a `1kg` (normalized weight) 
package to Philipp directly at a cost of `11,31 EUR` or you could ship through Adam for a 
total cost of `4,12 EUR`. Shipping through Adam is the optimal cost.

## TASK

Given a package, a network of friends with the shipping `HARD` units, and a friend to which 
you want to ship a package, write a Java program which always finds the lowest shipping cost 
possible from you to that friend.

Your program should have a comprehensive documentation so that it's obvious and intuitive for 
Allpago to run it. Provide unit tests. At minimum, you must provide a unit test capable of consuming a 
`CSV` file with definition of a your friend network and expected results. The format of a file is:
```
SOURCE,TARGET:HARD,TARGET:HARD ... 
@,TARGET,PACKAGE,COST
```
Both lines can appear multiple times. `SOURCE` is You or a friend for whom other friends and 
their respective hardness relationship is defined. You are always represented by the string 
`ME`. Your friends are always represented by first name. Friends with same first name are appended 
a differentiator such as a sequence (Jessica1, Jessica2, Jessica3, etc). For example, you could 
define your friend network in a `CSV` like this:
```
ME,Lisa:33,Peter:123,John:55 
Lisa,John:3 
Diana,Peter:11
```
Given example above, you have three friends: Lisa, Peter and John. You can ship to Lisa @ `33 HARD`, 
to Peter @ `123 HARD` and to John @ `55 HARD`. In addition, Lisa shipment to John is `3 HARD` 
and Diana can ship to Peter @ `11 HARD`. You cannot ship to Diana.

If the line begins with the `@` symbol, then it is an assertion line which means you are given 
a scenario which your test must pass. Each scenario will always be the correct shipping cost from You 
to someone in your network given package information. For example:
```
@,Lisa,10x9x5x1200,6.89 
@,Diana,6x10x8x1233,~
```
The first assertion states that a shipping cost for a `1.2kg (width=10cm, length=9cm, height=5cm)` 
package to Lisa should be `6.89 EUR`. The second assertion states that it's impossible to ship a 
package to Diana so the cost is a positive infinity.

You are not allowed to modify provided CSV files in any way.


## Requirements

You'll need Java 8 to compile and run the application. You'll also need maven to build it.

## How do I run it?

You can run the application using



Or you can build the JAR file with

```
mvn clean package
```
and run the JAR by typing
```
java -jar target/shortest-path-1.0-SNAPSHOT-jar-with-dependencies.jar -input 01.csv
```
Give the input file(s) as program parameters by specifying -input fileName
The file name can eithe be a single file or a directory. In the later case it will try and parse every .csv file inside
the directory and run and produce some results for every single file of the program.

## Implementation Details



## Allowed API operations


    
  
You can find some usage examples of the tool specifically for our application under examples/request-examples.txt

ENJOY!
