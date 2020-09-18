# currentlimiting
一个基于令牌桶算法的限流项目

这个项目用于对系统接口的限流，例如限制某个接口每秒访问不超过100次。

接口限流的流程大致如下：
1.限流器以固定速率生产令牌，存放到令牌桶里，令牌桶有容量限制，超过容量的令牌不会保存；
2.通过spring的aop拦截接口的请求；
3.尝试从令牌桶获取一个或多个令牌；
4.如果获取到就允许访问接口，如果获取不到就拒绝访问接口；



这个项目实现了两种限流器，一种是基于Google Guava的限流器实现的，另一种是基于redis和lua实现的。
基于Google Guava的限流器仅适用于单个服务器。
基于redis和lua的限流器适用于单个服务器和分布式服务器。

基于Google Guava的限流器:
实现类路径为com.example.currentlimiting.rateLimiterService.rateLimiterService，实现比较简单。

基于redis和lua的限流器：
实现类路径为com.example.currentlimiting.rateLimiterService.redisRateLimiterService，需要了解令牌桶算法。
这个限流器是的令牌生成和发放都是在redis的lua脚本里完成，利用redis的lua脚本保证令牌生成和发放的原子性。




使用注意事项：
1.使用基于redis和lua的限流器前，需要在application.properties里配置redis服务器和限流器的各项参数，根据注释配置就行；
2.项目默认使用基于redis和lua的限流器，如需使用基于Google Guava的限流器，在com.example.currentlimiting.aspect.currentLimitingAspect里
  修改限流器的@Qualifier("redisRateLimiterService")为@Qualifier("rateLimiterService")；
3.限流接口在com.example.currentlimiting.aspect.currentLimitingAspect切面的@Pointcut()里配置；
4.由于redis的备份和同步机制，在lua中获取的本地时间后不能执行写操作，所以lua脚本的时间是从服务器传过来的，不是redis本地的时间。
