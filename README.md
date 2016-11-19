# 介绍

本项目包含一个工具类`CNIPRecognizer`，用于判断某个ip是否为国内ip。判断原理很简单，如果该ip存在于[apnic](ftp://ftp.apnic.net/public/stats/apnic/delegated-apnic-latest)公开的cn地址段中则认为它是国内ip。示例代码：
```
System.out.println("8.8.8.8: " + CNIPRecognizer.isCNIP("8.8.8.8"));
System.out.println("114.114.114.114: " + CNIPRecognizer.isCNIP("114.114.114.114"));
```

# 如何更新[apnic](ftp://ftp.apnic.net/public/stats/apnic/delegated-apnic-latest)地址段？

通过如下Scala代码生成相应的Java代码，然后将生成的代码覆盖`CNIPRecognizer`类中相应部分：
```
Source.fromFile("D:/delegated-apnic-latest", "utf-8").getLines()
  .filter(_.startsWith("apnic|CN|ipv4|"))
  .map(_.split("\\|")).filter(_.length == 7)
  .map(arr => (IPUtil.ipToLong(arr(3)), arr(4).toLong))
  .toList.sortBy(_._1)
  .foldLeft(List.empty[(Long, Long)]){ (list, t) =>
    list match {
      case head :: tail =>
        if(head._1 + head._2 == t._1){ head.copy(_2 = head._2 + t._2) :: tail } else { t :: list }
      case Nil =>
        t :: Nil
    }
  }.reverse.zipWithIndex.foreach{ case (t, i) =>{
  if(i % 1000 == 0){
    if(i > 0){
      println("}")
    }
    println(s"private static void init${i/1000}(List<CNRecord> list){")
  }
  println(s"list.add(new CNRecord(${t._1}L, ${t._2}));")
}}
println("}")
```
为了减少生成的代码行数，针对原始的[apnic](ftp://ftp.apnic.net/public/stats/apnic/delegated-apnic-latest)进行了ip地址段合并，合并前的地址段有7637条记录，合并后剩余3365条。

