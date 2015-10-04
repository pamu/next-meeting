import Libraries.android._
import Libraries.macroid._
import Libraries.playServices._
import Libraries.apacheCommons._
import Libraries.json._
import Libraries.test._
import android.Keys._

android.Plugin.androidBuild

platformTarget in Android := Versions.androidPlatformV

name := """MeetingRoom"""

organization := "com.pamu_nagarjuna"

organizationName := "247"

organizationHomepage := Some(new URL("http://pamu.github.io"))

version := Versions.appV

scalaVersion := Versions.scalaV

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers ++= Settings.resolvers

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-feature", "-deprecation", "-target:jvm-1.7")

libraryDependencies ++= Seq(
  aar(macroidRoot),
  aar(androidAppCompat),
  aar(androidCardView),
  aar(androidRecyclerview),
  aar(macroidExtras),
  aar(playServicesBase),
  apacheCommonsLang,
  //json4s,
  playJson,
  specs2,
  mockito,
  androidTest,
  compilerPlugin(Libraries.wartRemover))

run <<= run in Android

packagingOptions in Android := PackagingOptions(Seq(
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt"), Seq.empty[String], Seq.empty[String])

packageRelease <<= packageRelease in Android

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Settings.proguardCommons

proguardCache in Android := Seq.empty