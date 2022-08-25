package com.sa.product.test;

import com.ql.util.express.*;
import com.ql.util.express.config.QLExpressRunStrategy;
import com.ql.util.express.exception.QLException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QLExpressTest {
    //- 不支持try{}catch{}
    //- 注释目前只支持 /** **/，不支持单行注释 //
    //- 不支持java8的lambda表达式
    //- 不支持for循环集合操作for (Item item : list)
    //- 弱类型语言，请不要定义类型声明,更不要用Template（Map<String, List>之类的）
    //- array的声明不一样 使用[]
    //- min,max,round,print,println,like,in 都是系统默认函数的关键字，请不要作为变量名

    /**
     * 测试
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        ExpressRunner expressRunner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a",1);
        context.put("b",1);
        context.put("c",1);
        Object execute = expressRunner.execute("a+b+c", context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试循环
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "n=100;sum=0;" +
                "for(i=1;i<=n;i++){" +
                "sum = sum+i;" +
                "}" +
                "return sum;";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试三目运算符
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        context.put("a",100);
        context.put("b",101);
        String express =  "a>b?a:b";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试数组
     * 声明的方式类似arr=new int[3];
     * 声明并赋值的时候和java有点不同
     *      mins=[5,30];
     * 迭代的时候属性叫做length
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "arr=new int[3];arr1=new int[5];arr2=[5,30];" +
                "arr[0]=1;arr[1]=2;arr[2]=3;" +
                "return Arrays.toString(arr1);";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试list
     * new ArrayList(); 不用范型
     * 迭代的时候属性list.size()
     * 取的时候用list.get(i)
     * 不支持 += -=
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "list = new ArrayList();" +
                "list.add(3);list.add(1);list.add(2);" +
                "sum = 0;" +
                "for(i=0;i<list.size();i++){" +
                "sum = sum+list.get(i);" +
                "}" +
                "return sum;";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试map
     * 不使用范型 new HashMap()
     * 迭代的时候先生成keySet
     * keySet.toArray()获取keyArray
     * 迭代keyArray获取key
     * 通过key获取value
     * 不支持 += -=
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "map=new HashMap();" +
                "map.put('a',2);map.put('b',2);map.put('c',2);" +
                "sum=0;" +
                "keySet=map.keySet();" +
                "keyArray=keySet.toArray();" +
                "for(i=0;i<keyArray.length;i++){" +
                "sum=sum+map.get(keyArray[i]);" +
                "}" +
                "return sum;";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }
    /**
     * 测试JavaBean
     * 需要import引入依赖
     * 系统自动会import java.lang.,import java.util.;
     * 创建对象后可以调用静态和非静态的方法
     * @throws Exception
     */
    @Test
    public void test7() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "import com.sa.product.test.Person;" +
                "Person person = new Person();" +
                "person.getNumber();" +
                "person.sayHello();";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试Function
     * 在入参的地方需要声明类型
     * function的右}需要添加;
     * @throws Exception
     */
    @Test
    public void test8() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        String express =  "function add(int a, int b){" +
                "return a+b;" +
                "};" +
                "return add(2,2);";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试Operator
     * 替换操作符
     * @throws Exception
     */
    @Test
    public void test9() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        expressRunner.addOperatorWithAlias("如果", "if", null);
        expressRunner.addOperatorWithAlias("则", "then", null);
        expressRunner.addOperatorWithAlias("否则", "else", null);
        expressRunner.addOperatorWithAlias("返回", "return", null);
        context.put("语文",100);
        context.put("数学",100);
        context.put("英语",100);
        String express =  "如果((语文+数学+英语)>270) 则 返回 1; 否则 返回 0;";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试自定义Operator
     * 添加一个操作符
     * @throws Exception
     */
    @Test
    public void test10() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        //添加一个操作符: join 走后边的逻辑
        //运算的时候从左到右, 前两个运算符先计算, 再和下一个数据进行计算
        expressRunner.addOperator("join",new JoinOperator());

        String express =  "1 join 2 join 3";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 测试自定义Operator
     * 将 + 操作符修改成 自定义类型
     * @throws Exception
     */
    @Test
    public void test11() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        //修改操作符: + 修改成 join
        //运算的时候从左到右, 前两个运算符先计算, 再和下一个数据进行计算
        expressRunner.replaceOperator("+",new JoinOperator());
        String express =  "1 + 2 + 3";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }


    /**
     * 测试自定义Operator
     * 添加一个 方法
     * @throws Exception
     */
    @Test
    public void test12() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        //添加一个方法 : join(...)
        // 会将所有的参数传递到后台
        expressRunner.addFunction("join",new JoinOperator());
        String express =  "join(1,2,3)";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }


    /**
     * 绑定静态方法和实例方法
     * @throws Exception
     */
    @Test
    public void test13() throws Exception {
        //创建一个规则验证器
        ExpressRunner expressRunner = new ExpressRunner();
        //用来存储数据
        DefaultContext<String, Object> context = new DefaultContext<>();
        //添加一个方法 : join(...)
        // 会将所有的参数传递到后台
        expressRunner.addFunctionOfClassMethod("取绝对值",Math.class.getName(),"abs",new String[]{"double"},null);
        expressRunner.addFunctionOfClassMethod("转大写", BeanExample.class.getName(),"upper",new String[]{"String"},null);
        expressRunner.addFunctionOfServiceMethod("打印",System.out,"println",new String[]{"String"},null);
        expressRunner.addFunctionOfServiceMethod("是否存在",new BeanExample(),"contains",new String[]{},null);
        String express = "取绝对值(-100);转大写(\"hello world\");打印(\"你好吗？\");是否存在();";
        Object execute = expressRunner.execute(express, context, null, true, false);
        System.out.println(execute);
    }

    /**
     * 给现有的类添加方法
     * @throws Exception
     */
    @Test
    public void test14() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        //给类增加方法
        runner.addFunctionAndClassMethod("isBlank", String.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                Object obj = list[0];
                if (obj == null) {
                    return true;
                }

                String str = String.valueOf(obj);
                return str.length() == 0;
            }
        });
        //"".isBlank
        String express = "a=\"\".isBlank()";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }
    /**
     * 定义宏
     * @throws Exception
     */
    @Test
    public void test15() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String, Object> context = new DefaultContext<String, Object>();

        runner.addMacro("计算平均成绩", "(语文+数学+英语)/3.0");
        runner.addMacro("是否优秀", "计算平均成绩>90");
        context.put("语文", 88);
        context.put("数学", 99);
        context.put("英语", 95);
        Object result = runner.execute("是否优秀", context, null, false, false);
        System.out.println(result);
    }

    /**
     * javaClass
     * //添加类的属性字段
     * void addClassField(String field,Class<?>bindingClass,Class<?>returnType,Operator op);
     *
     * //添加类的方法
     * void addClassMethod(String name,Class<?>bindingClass,OperatorBase op);
     *
     * 注意，这些类的字段和方法是执行器通过解析语法执行的，而不是通过字节码增强等技术，所以只在脚本运行期间生效，不会对jvm整体的运行产生任何影响，所以是绝对安全的
     * @throws Exception
     */
    @Test
    public void testClass1() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String, Object> context = new DefaultContext<String, Object>();
        runner.addClassMethod("join", java.util.List.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                ArrayList arrayList = (ArrayList)list[0];
                return StringUtils.join(arrayList,list[1]);
            }
        });
        runner.addClassMethod("join", java.util.Map.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                HashMap map = (HashMap) list[0];
                StringBuilder sb = new StringBuilder();
                for(Object key: map.keySet()){
                    sb.append(key).append("=").append(map.get(key)).append((String) list[1]);
                }
                return sb.substring(0,sb.length()-1);
            }
        });


        runner.addClassMethod("链式加载", java.util.Map.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                HashMap map = (HashMap) list[0];
                map.put(list[1],list[2]);
                return map;
            }
        });
        //优先使用缓存中的指令集
//        InstructionSet localCache = runner.getInstructionSetFromLocalCache("");
//        Object execute = runner.execute(localCache,context, (List<String>) null,false,false,);

        Object result = runner.execute("list=new ArrayList();list.add(1);list.add(2);list.add(3);return list.join(' , ');", context, null, false, false);
        System.out.println(result);
        result = runner.execute("list=new HashMap();list.put('a',1);list.put('b',2);list.put('c',3);return list.join(' , ');",context,null,false,false);
        System.out.println(result);
        String express = "map=new HashMap();map.链式加载('a',1).链式加载('b',2).链式加载('c',3); " +
                "keySet = map.keySet();\n" +
                "objArr = keySet.toArray();\n" +
                "for (i = 0; i < objArr.length; i++) {\n" +
                "    key = objArr[i];\n" +
                "    System.out.println(key+' : '+map.get(key));\n" +
                "}";
        runner.execute(express,context,null,false,false);
    }

    /**
     * 使用这种完成AOP
     * @throws Exception
     */
    @Test
    public void testAop() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        IExpressContext<String,Object> context = new DefaultContext<String, Object>();

        runner.addClassMethod("size", java.util.List.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                ArrayList arrayList = (ArrayList) list[0];
                System.out.println("拦截到List.size()方法");
                return arrayList.size();
            }
        });

        runner.addClassField("长度", java.util.List.class, new Operator() {
            @Override
            public Object executeInner(Object[] list) throws Exception {
                ArrayList arrayList = (ArrayList) list[0];
                System.out.println("拦截到List.长度 字段的计算");
                return arrayList.size();
            }
        });
        Object result = runner.execute("list=new ArrayList();list.add(1);list.add(2);list.add(3);return list.size();",context,null,false,false);
        System.out.println(result);
        result  = runner.execute("list=new ArrayList();list.add(1);list.add(2);list.add(3);return list.长度;",context,null,false,false);
        System.out.println(result);

        //bugfix 没有return 的时候可能会多次调用getType，并且返回错误
        Object result2  = runner.execute("list=new ArrayList();list.add(1);list.add(2);list.add(3);list.长度;",context,null,false,false);
        System.out.println(result2);
    }








    /**
     * 获取需要定义的方法和属性
     * @throws Exception
     */
    @Test
    public void test16() throws Exception {
        String express = "int 平均分 = (语文+数学+英语+综合考试.科目2)/4.0;return 平均分";
        ExpressRunner runner = new ExpressRunner(true, true);
        String[] names = runner.getOutVarNames(express);
        for (String s : names) {
            System.out.println("var : " + s);
        }

        String[] functions = runner.getOutFunctionNames(express);
        for (String s : functions) {
            System.out.println("function : " + s);
        }
    }

    /**
     * 关于不定参数的使用
     * 这种不定参数的类型 默认可以使用数组进行替代
     * @throws Exception
     */
    @Test
    public void test17() throws Exception {
        ExpressRunner runner = new ExpressRunner(true, true);
        DefaultContext<String,Object> defaultContext = new DefaultContext<>();
        runner.addFunctionOfServiceMethod("获取模板",new BeanExample(),"getTemplate",new Class[]{Object[].class},null);
        Object execute = runner.execute("获取模板([11,'22',33L,true])", defaultContext, null, false, false);
        System.out.println(execute);
    }

    /**
     * 关于不定参数的使用
     * 这种不定参数的类型 可以开启动态参数
     * @throws Exception
     */
    @Test
    public void test18() throws Exception {
        //开启动态参数
        DynamicParamsUtil.supportDynamicParams = true;
        ExpressRunner runner = new ExpressRunner(true, true);
        DefaultContext<String,Object> defaultContext = new DefaultContext<>();
        runner.addFunctionOfServiceMethod("获取模板",new BeanExample(),"getTemplate",new Class[]{Object[].class},null);
        Object execute = runner.execute("获取模板(11,'22',33L,true)", defaultContext, null, false, false);
        System.out.println(execute);
    }

    /**
     * 关于集合的快捷写法
     * @throws Exception
     */
    @Test
    public void test19() throws Exception {
        ExpressRunner runner = new ExpressRunner(false, false);
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        //value = map.get(key)
        String express = "abc = NewMap(1:1, 2:2); return abc.get(1) + abc.get(2);";
        Object r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        express = "abc = NewList(1, 2, 3); return abc.get(1) + abc.get(2)";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
        express = "abc = [1, 2, 3]; return abc[1] + abc[2];";
        r = runner.execute(express, context, null, false, false);
        System.out.println(r);
    }

    /**
     * 集合的遍历
     * @throws Exception
     */
    @Test
    public void test20() throws Exception {
        ExpressRunner expressRunner = new ExpressRunner();
        DefaultContext<String,Object> context = new DefaultContext();
        String express = "map = new HashMap();\n" +
                "map.put(\"a\", \"a_value\");\n" +
                "map.put(\"b\", \"b_value\");\n" +
                "keySet = map.keySet();\n" +
                "objArr = keySet.toArray();\n" +
                "for (i = 0; i < objArr.length; i++) {\n" +
                "    key = objArr[i];\n" +
                "    System.out.println(map.get(key));\n" +
                "}";
        expressRunner.execute(express, context, null, true, false);
    }


    /**
     * 测试短路逻辑, 并且输出错误信息
     *  2  小于  1  不满足期望
     *  runner.setShortCircuit(true); 使用这种方式就可以实现逻辑短路
     * @throws Exception
     */
    @Test
    public void test21() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.setShortCircuit(true);
        runner.addOperatorWithAlias("小于", "<", "$1 小于 $2 不满足期望");
        runner.addOperatorWithAlias("大于", ">", "$1 大于 $2 不满足期望");
        DefaultContext<String,Object> context = new DefaultContext<>();
        context.put("迟到天数",2);
        context.put("出勤天数",13);
        context.put("工作时间",15);
        String express = "2 小于 1 and (迟到天数 大于 90 or 出勤天数 大于 10)";
        List<String> error = new ArrayList<>();
        Boolean result = (Boolean)runner.execute(express, context, error, true, false);
        if (result){
            System.out.println("result is true");
        }else{
            System.out.println("result is false");
            for (String e:error){
                System.out.println(e);
            }
        }
    }
    /**
     * 测试非短路逻辑, 并且输出错误信息
     * 输出结果
     *   2  小于  1  不满足期望
     *  迟到天数:2  大于  90  不满足期望
     *  runner.setShortCircuit(false); 使用这种方式逻辑判断就不短路了
     * @throws Exception
     */
    @Test
    public void test22() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.setShortCircuit(false);
        runner.addOperatorWithAlias("小于", "<", "$1 小于 $2 不满足期望");
        runner.addOperatorWithAlias("大于", ">", "$1 大于 $2 不满足期望");
        DefaultContext<String,Object> context = new DefaultContext<>();
        context.put("迟到天数",2);
        context.put("出勤天数",13);
        context.put("工作时间",15);
        String express = "2 小于 1 and (迟到天数 大于 90 or 出勤天数 大于 10)";
        List<String> error = new ArrayList<>();
        Boolean result = (Boolean)runner.execute(express, context, error, true, false);
        if (result){
            System.out.println("result is true");
        }else{
            System.out.println("result is false");
            for (String e:error){
                System.out.println(e);
            }
        }
    }

    /**
     * 用于编译指令集的接口
     * runner.parseInstructionSet(expressString);
     * 可以使用这个接口进行ql脚本的校验, 如果校验没有报错那就说明这个express身体日那个是可以运行的
     * @throws Exception
     */
    @Test
    public void test23() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        String expressString = "for(i = 0; i < 10; i++) {sum = i + 1;} return sum;";
        InstructionSet instructionSet = runner.parseInstructionSet(expressString);
        System.out.println(instructionSet);
    }

    /**
     * 防止死循环
     */
    @Test
    public void test24() throws Exception{
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String,Object> context = new DefaultContext<>();

        try {
            String express = "sum = 0; for(i = 0; i < 1000; i++) {sum = sum + i;} return sum;";
            //可通过timeoutMillis参数设置脚本的运行超时时间:1000ms
            Object r = runner.execute(express, context, null, true, false, 1000);
            System.out.println(r);
            throw new Exception("没有捕获到超时异常");
        } catch (QLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test25() throws Exception{
        ExpressRunner runner = new ExpressRunner();
        //设置禁止调用不安全的系统方法
        QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);

        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        try {
            String express = "System.exit(1);";
            Object r = runner.execute(express, context, null, true, false);
            System.out.println(r);
            throw new Exception("没有捕获到不安全的方法");
        } catch (QLException e) {
            System.out.println(e);
        }
    }


    @Test
    public void test26() throws Exception{
        List<Long> ids = new ArrayList<>();
        ExpressRunner runner = new ExpressRunner();
        runner.replaceOperator("==",new EqualOperate());
        QLExpressRunStrategy.setForbidInvokeSecurityRiskMethods(true);
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        String express = "a='123'=='123';" +
                "b= NewList(1, 2, 3)== NewList(1, 2, 3);";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
        express = "徐123";
        Pattern p = Pattern.compile("徐" + ".*?([0-9|,]+)");
        Matcher m = p.matcher(express);
        while (m.find()) {
            String group = m.group(1);
            if (group.contains(",")) {
                ids.addAll(Arrays.stream(group.split(",")).map(Long::parseLong).collect(Collectors.toList()));
            } else {
                ids.add(Long.parseLong(m.group(1)));
            }
        }
    }
    @Test
    public void testOptional(){
        Insurance insurance = new Insurance();
        insurance.setName(null);
        Optional<Insurance> optionalInsurance = Optional.ofNullable(null);
        String name = optionalInsurance.get().getName();
        System.out.println(name);
    }
    static class Insurance{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
