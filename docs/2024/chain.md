# 在Java业务中使用责任链模式

![chain-of-responsibility.png](https://s2.loli.net/2024/04/08/KADjIVlfJsp7dRh.png)

> 说来惭愧，干了这么久，设计模式都很少在业务代码中出现过，最近在业务中用到了关于责任链模式，浅浅的记录一下。

---

## 业务说明

最近有一个这种业务，需要校验导入的excel数据，校验规则很多，校验顺序也很复杂，所以就写了一个责任链模式，其实发现以前的代码中也能用到这一块。

---

## 代码实现

```chain.java
public abstract class CostTableChain {

    public abstract void doHandle(CostTableImportVo importVo, CostTableExcelVo excelVo);

    public abstract int order();
}
```

```chain-first.java
@Service
public class CheckSegmentCbsCodeHandler extends CostTableChain {

    @Override
    public void doHandle(CostTableImportVo importVo, CostTableExcelVo excelVo) {
    }
    
    @Override
    public int order() {
        return 1;
    }
}
```

```invoke.java
    @Autowired
    private List<CostTableChain> handlers;
    
    handlers.stream().sorted(Comparator.comparingInt(CostTableChain::order)).forEach(e -> e.doHandle(importVo, costTableInfo));
```

---

## 代码说明

1. CostTableChain：责任链模式抽象类，定义了责任链模式中每个节点的抽象方法，以及排序方法。
2. CheckSegmentCbsCodeHandler：具体责任链模式实现类，实现了抽象类中的抽象方法，并定义了排序方法。
3. invoke：调用方法，通过注入的方式，将所有实现类注入到集合中，然后通过排序方法，将集合中的实现类按照顺序排序，然后遍历集合中的实现类，调用实现类的抽象方法。

> 这里的参数是传递的固定业务的VO对象，如果想传通用对象，也可以定义一个abstract类，实现一个自定义的interface，然后实现这个interface，然后注入到集合中。

---

## 总结

还是需要一直学习，代码永远在进步。
