# Why ByteCode

I make this craft about 15 minutes and plan to finish it in a day. Just have a try!
But I think it's too simple for not knowing too much about this topic, so better to 
learn a technique to split a complex task into bunches of little bit hard tasks. What
do I lost? Patience & Persistence!

> to manipulate class element - method, field and etc.

Some facts about byte code manipulation:

- mybatis uses javaassist, I don't know why
- implement AOP, I don't know how
- ThreadWeaver: change thread execute logic
- cglib seems very popular
- motan: create non-exist class and method
- lombok: create setter-getter method, builder... 

Define byte code:

> instructions that JVM execute

Core API: not easy to find good stuff about it. So I just dive into ASM

# Task Tree

How to manipulate:
  - Create a class file
  - Create or modify fields
  - Create or modify methods 
   
How to impl AOP:
  - Learn some AOP, I did it several time, try to handle this issues
  - Write lots of executes, find general rules for executes expression
  - Try to dig source code and build my own AOP

How to build lombok?
  - Dig setter & impl
  - Dig builder & impl
  
# Q&A

- How to locate insert or modify place fast?
- How to tell JVM load or reload class modified?
- How to handle Java8 lambda expressions?