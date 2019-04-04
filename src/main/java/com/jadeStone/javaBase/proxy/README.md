# 代理
明星的经纪人，大佬的秘书；你只能通过代理才能和你的目标发生关系。
<br/>
[代理模式的类图](https://images2015.cnblogs.com/blog/527668/201601/527668-20160109145901215-69139001.jpg)
## 静态代理
### 代码：

<details>
<summary>
相关代码：
</summary>

```
public class StaticDemo {

	public static void main(String[] args) {
		DoService doService = new DoSerciceImpl();
		doService.doo();
		// 代理：1.还是原来的引用，即对客户端透明）；-----> 实现同一个接口
		// 	   2. 原来的功能不变，加入了新的功能；-----> 同接口同方法，组合增强功能
		// 
		doService = new ProxyImpl(doService);
		doService.doo();
	}
}
interface DoService{
	void doo();
}

class DoSerciceImpl implements DoService{

	@Override
	public void doo() {
		System.out.println("doo");
	}
}

class ProxyImpl implements DoService{

	DoService doService;
	public ProxyImpl(DoService doService) {
		this.doService = doService;
	}
	@Override
	public void doo() {
		System.out.println("before doo");
		doService.doo();
		System.out.println("after doo");
	}
}
```

</details>

### 关键点：
- 实现同样的接口：对客户端透明
### 反思：
- 使用了组合，每个服务都需要一个与之配对的代理类。
## 动态代理
-   运行时才生成相关的代理类；
-   相同接口的会缓存下来，避免重复生成代理类；
-   代理类拥有统一的父类Proxy，都会有一个成员变量InvocationHandler h；每次调用代理类的方法都会被转发到他的invoke(Object proxy, Method method, Object[] args)方法，利用反射实现方法的调用。
-   invoke方法的返回值需要注意一下，是接口定义的返回值类型(的子类)/或者拆装箱,类型不符合还会报错。
-   基本使用方法：

```
// 声明一个handler
InvocationHandler handler = new MyInvocationHandler(...);
// 生成一个代理类对象
Class proxyClass = Proxy.getProxyClass(doService.getClassLoader(),DoService.class);
// 代理类实例化
DoService f = (DoService)proxyClass.getConstructor(InvocationHandler.class).newInstance(handler);
```
或者更简单粗错的，也是我们最常使用的（其实只是对上面方法的一个封装，直接返回实例对象）

```
DoService f = (Foo) Proxy.newProxyInstance(doService.getClassLoader(),
      new Class[] { DoService.class },handler);
```

### 代码：
### 关键点：
### 反思：

### 补充：
- java保存动态代理生成的类的class文件
启动时加：
```
-Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
```
<details>
<summary>
本文生成的代理实例类的字节码：
</summary>

```
public final class $Proxy0 extends Proxy implements DoService {
   private static Method m1;
   private static Method m3;
   private static Method m2;
   private static Method m0;

   public $Proxy0(InvocationHandler arg0) throws  {
      super(arg0);
   }

   public final boolean equals(Object arg0) throws  {
      try {
         return ((Boolean)super.h.invoke(this, m1, new Object[]{arg0})).booleanValue();
      } catch (RuntimeException | Error arg2) {
         throw arg2;
      } catch (Throwable arg3) {
         throw new UndeclaredThrowableException(arg3);
      }
   }

   public final void doo() throws  {
      try {
         super.h.invoke(this, m3, (Object[])null);
      } catch (RuntimeException | Error arg1) {
         throw arg1;
      } catch (Throwable arg2) {
         throw new UndeclaredThrowableException(arg2);
      }
   }

   public final String toString() throws  {
      try {
         return (String)super.h.invoke(this, m2, (Object[])null);
      } catch (RuntimeException | Error arg1) {
         throw arg1;
      } catch (Throwable arg2) {
         throw new UndeclaredThrowableException(arg2);
      }
   }

   public final int hashCode() throws  {
      try {
         return ((Integer)super.h.invoke(this, m0, (Object[])null)).intValue();
      } catch (RuntimeException | Error arg1) {
         throw arg1;
      } catch (Throwable arg2) {
         throw new UndeclaredThrowableException(arg2);
      }
   }

   static {
      try {
         m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[]{Class.forName("java.lang.Object")});
         m3 = Class.forName("com.jadeStone.javaBase.proxy.dynamic.DoService").getMethod("doo", new Class[0]);
         m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
         m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
      } catch (NoSuchMethodException arg1) {
         throw new NoSuchMethodError(arg1.getMessage());
      } catch (ClassNotFoundException arg2) {
         throw new NoClassDefFoundError(arg2.getMessage());
      }
   }
}
```
</details>

-   markdown折叠代码，【summary】标签下记得空一行
