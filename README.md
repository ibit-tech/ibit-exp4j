# ibit-exp4j

## 简介

`ibit-exp4j` 主要是计算类库 [exp4j](https://www.objecthunter.net/exp4j/) 进一步封装，进而简化公式计算的繁琐构造。exp4j 的用法可参考 wiki  [exp4j 用法](https://wiki.ibit.tech/d/340#r304)。

## 主要类

### 公式计算相关

* [FormulaEntity](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/FormulaEntity.html)：定义公式实体

* [IVariable](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/valuebean/IVariable.html)：定义公式变量

* [FormulaEvaluator](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/FormulaEvaluator.html)：单个公式计算器

* [FormulaEvaluators](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/FormulaEvaluators.html)：批量公式计算器


### 关系相关

* [RelationEntity](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/RelationEntity.html)：关系实体

* [RelationEvaluators](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/RelationEvaluators.html)：关系计算器

### 自定义函数和操作符扩展工具类

* [MathFunctionEnhanceUtils](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/utils/MathFunctionEnhanceUtils.html)：数学函数扩展（rint,pow,min,max）

* [OperatorEnhanceUtils](https://ibit.tech/apidocs/ibit-exp4j/1.x/tech/ibit/exp4j/utils/OperatorEnhanceUtils.html)：操作符扩展（$,//,>,>=,==,<=,<）
