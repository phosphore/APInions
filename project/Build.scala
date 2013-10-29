import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "APInions"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.18"
    
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
          
  )
  


}
