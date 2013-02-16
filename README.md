# Java/Spring session concurrency test

This is an experiment to investigate how different synchronization locks behave in Java, Spring and http sessions.
It originates from my question on [StackOverflow](http://stackoverflow.com/questions/14890348/syncronizing-on-a-instance-variable-in-a-session-scoped-spring-bean-to-limit-con).

It simulates an application where each user fires multiple ajaxrequests in parallel to
the [SlowLegacyCarRepository](src/main/java/no/mobmad/SlowLegacyCarRepository.java). The SlowLegacyCarRepository can
only handle one concurrent request pr. user, hence the need to either:

* queue all ajax-requests on the client (not discussed further here). If ***most parts*** of your backend doesn't handle concurrent requests for some reason, this
might be a better approach than to synchronize on the server-side (see below).
* On the other hand, you might have a backend where most of the ajax-requests ***can*** run in parallel and queueing all
 ajax-requests seems like a sledgehammer solution. Instead you consider synchronizing the problematic requests, so that a maximum
 of one concurrent request hits the legacy service pr. session (user). ***Warning*** requests will be blocked (put on hold)
 until the legacy service returns and let another request in. This ***might*** be a problem if the client side application
 should fire R requests pr user and U users are doing this at the same time. [FEEDBACK/MORE RESEARCH NEEDED]

Synchronization should also work in a load balanced environment, as long as sticky sessions are enabled, forwarding each
 session to the same app server/JVM.

## Run instructions

1. `mvn jetty:run`
2. Fire up at least two browsers and tile windows for the best experience.
3. Click on:
    * ***no sync*** for no synchronization. The "backend service" will process multiple requests from the same user simultaneously
    * ***instance*** for synchronization on a instancevariable inside a session scoped Spring bean
    * ***autowired*** for synchronization on an autowired requestscoped instancevariable inside a session scoped Spring bean
    * ***sessionmutex*** for synchronization on a HttpSession attribute with Spring's WebUtils.getSessionMutex

![](screenshot.png)

Each run pr. browser (simulated user) is given a new HttpSession. Session will be logged with different colors for
 your viewing pleasure. The number behind the status, e.g. QUEUED (716) identifies requests pr. session in order to
 trace them easier.

## Configuration
Adjust the number of simultaneous requests and response time in [index.jsp](src/main/webapp/index.jsp)

## Preliminary results
Instance/autowired seem to work the same and as intended. As the screenshot shows, no more than one
request is processed in parallel pr. user. Compare this with "no sync" to see the difference. Using sessionmutex
works most of the time, but randomly fails (at least in Jetty) with one of these:

* java.lang.IllegalStateException: Problem scavenging sessions
* java.lang.IllegalStateException: No SessionManager

[MORE RESEARCH NEEDED])

## Related notes (mostly for myself)

* [Clustering vs. Load Balancing – What is the difference?](http://standardwisdom.com/softwarejournal/2009/09/clustering-vs-load-balancing-what-is-the-difference/)
* [Load balancing with Jetty](http://docs.codehaus.org/display/JETTY/Configuring+mod_proxy) and [Apache mod_proxy / mod_proxy_balancer](http://httpd.apache.org/docs/2.2/mod/mod_proxy_balancer.html)
* [Unicast vs. multicast](http://stackoverflow.com/questions/4338475/why-use-unicast-versus-multicast-in-weblogic-clusters)
* [Loan balancing in a cluster](http://docs.oracle.com/cd/E11035_01/wls100/cluster/load_balancing.html)

## Contributions
Feedback and pull requests are most welcome :-)
