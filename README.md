<img src="https://github.com/BlackBoxVision/mvp-helpers/blob/master/art/logo.png" width="425px" height="125px">
> Helper classes to build Android Apps through MVP pattern in a faster way

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MVP%20Helpers-brightgreen.svg?style=flat)]() [![](https://jitpack.io/v/BlackBoxVision/mvp-helpers.svg)](https://jitpack.io/#BlackBoxVision/mvp-helpers) [![Build Status](https://travis-ci.org/BlackBoxVision/mvp-helpers.svg?branch=master)](https://travis-ci.org/BlackBoxVision/mvp-helpers)

##Intro

After working about 3 years with Android development, I learn a lot from the different projects I have made, a lot of mistakes, and a lot of lessons learned. About 1 year ago, or more, in Android, MVP become the selected pattern to make Android Apps. MVP makes your apps code more easier to follow, and also easier to reason. 

This library, exposes a minimal API that I have abstracted during many projects. The classes that contain this library help me to speed app development, also, they clean my code a lot. 

Check the following instructions under this **README** in order to get a project up and running with this simpler library.

##Installation

Actually I don't have this library in **JCenter/Maven Central**, so if you want to use, follow the instructions. The library is distributed for Java and Kotlin.

- Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { 
			url "https://jitpack.io" 
		}
	}
}
```

- Add the dependency:
```gradle
dependencies {
	 compile 'com.github.BlackBoxVision:mvp-helpers:v0.0.1-kt'
}
```

**Maven**

- Add this line to repositories section in pom.xml:
```xml
<repositories>
	<repository>
	   <id>jitpack.io</id>
		 <url>https://jitpack.io</url>
	</repository>
</repositories>
```
- Add the dependency:
```xml
<dependency>
  <groupId>com.github.BlackBoxVision</groupId>
  <artifactId>mvp-helpers</artifactId>
	<version>v0.0.1-kt</version>
</dependency>
```

**SBT**

- Add it in your build.sbt at the end of resolvers:
```sbt
  resolvers += "jitpack" at "https://jitpack.io"
```

- Add the dependency:
```sbt
  libraryDependencies += "com.github.BlackBoxVision" % "mvp-helpers" % "v0.0.1-kt"	
```

##Core Concepts

The concepts behind this library are the following ones: 
- **View** → The **View** is an interface that contains methods related to UI interaction. Those methods should be implemented in your **Activity, Fragment or View**.

- **Interactor** → The **Interactor** is the class that do the hard work, all the blocking operations like **I/O, Networking, Database** Intectations should be done here. 

- **Presenter** → The **presenter** acts as a middle man between the **Interactor** and the **View**.

##Usage example

The usage is really simple, the concepts behind this library are the following ones: 

**1** - Create your **View** interface by extending the [**BaseView**](https://github.com/BlackBoxVision/mvp-helpers/blob/kotlin/library/src/main/java/io/blackbox_vision/mvphelpers/logic/view/BaseView.kt). **BaseView** is an empty interface that acts as water mark for the **Presenter**.

```java
interface DetailsView : BaseView {

    fun onInfoReceived(information: Bundle)

    fun onInfoError(errorMessage: String)
}
```

**2** - Create an **Interactor** class by extending the [**BaseInteractor**](https://github.com/BlackBoxVision/mvp-helpers/blob/kotlin/library/src/main/java/io/blackbox_vision/mvphelpers/logic/interactor/BaseInteractor.kt) class. The **BaseInteractor** provides you a set of helper methods to deal with **background execution and UIThread interaction**. The methods are the following ones: 

- **runOnUiThread** → use it when you need to post data to the main thread.
- **runOnBackground** → use it when you need to make background processing. 
- **runOnBackground** → use it when you need to make a scheduled task. 
- **cancelTask** → use it when you want to cancel a scheduled task. 

```java
class DetailsInteractor : BaseInteractor() {

    fun retrieveDetailsFromService(id: String, successListener: OnSuccessListener<Bundle>, errorListener: OnErrorListener<String>) {
        runOnBackground(Runnable {
            //Getting data from somewhere
            val data = MockUtils.getMockedData(id)

            runOnUiThread(Runnable {
                if (data != null) {
                    successListener.onSuccess(data)
                } else {
                    errorListener.onError("Ups, something went wrong")
                }
            })
        })
    }
}
```
**3** - Create a **Presenter** class by extending the [**BasePresenter**](https://github.com/BlackBoxVision/mvp-helpers/blob/kotlin/library/src/main/java/io/blackbox_vision/mvphelpers/logic/presenter/BasePresenter.kt) class. The **BasePresenter** provides you with a set of helper methods to deal with **View** management. The methods are the following ones:

- **isViewAttached** → check if you have set the view to the presenter, returns to you a boolean value that you should handle in your presenter implementation. 
- **attachView** → add the view to the presenter, so you can start to handle the cicle of view - presenter - interactor interaction.
- **detachView** → dereference the view, setting it to null. This method should be called in the onDestroy method in case of use in Activity, and onDestroyView in case of Fragment usage. 
- **getView** → simple getter, to make your access to the view defined more cleaner.

```java
class DetailsPresenter : BasePresenter<DetailsView>(), OnSuccessListener<Bundle>, OnErrorListener<String> {
    private val interactor: DetailsInteractor

    init {
        interactor = DetailsInteractor()
    }

    fun findRequiredInformation(id: String) {
        if (isViewAttached) {
            interactor.retrieveDetailsFromService(id, this, this)
        }
    }

    override fun onSuccess(data: Bundle) {
        if (isViewAttached) {
            view!!.onInfoReceived(data)
        }
    }

    override fun onError(error: String) {
        if (isViewAttached) {
            view!!.onInfoError(error)
        }
    }
}
```

**4** - Attach this cycle with Android specific classes. You can choice an **Activity/Fragment or also a custom view**. In this case I will show you an example with **Fragment** that inherits from [**BaseFragment**](https://github.com/BlackBoxVision/mvp-helpers/blob/kotlin/library/src/main/java/io/blackbox_vision/mvphelpers/ui/fragment/BaseFragment.kt)

The **BaseFragment** comes with a resumed lifecycle, and a set of methods to implement. The methods are the following ones:

- **addPresenter** → in this method you have to create you instance of Presenter. 
- **getLayout** → in this method you have pass the id reference to the layout. This library comes with **ButterKnife**, to provide efficiency I have implemented **onCreateView** in BaseFragment where I call **ButterKnife.bind** method, so you have view binding out of the box! :smile:
- **getPresenter** → simple getter, to make your access to the presenter more cleaner.
- **onPresenterCreated** → In this method you should attach the view to the presenter in order to start working.

```java
class DetailsFragment : BaseFragment<DetailsPresenter>(), DetailsView {
    override fun addPresenter(): DetailsPresenter {
        return DetailsPresenter()
    }

    override val layout: Int
        @LayoutRes
        get() = R.layout.fragment_details

    override fun onPresenterCreated(presenter: DetailsPresenter) {
        presenter.attachView(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
	presenter!!.findRequiredInformation("ssdWRGD132")
    }

    override fun onInfoReceived(information: Bundle) {
        Toast.makeText(context, "This is the information ---> ${information.get(MockUtils.SAMPLE)!!}", Toast.LENGTH_LONG).show()
    }

    override fun onInfoError(errorMessage: String) {
        Toast.makeText(context, "This is the error ---> $errorMessage", Toast.LENGTH_SHORT).show()
    }
}
```

##Some notes on ButterKnife

The standard **ButterKnife** library is included by default. But there is a missing point, you have to add in your app **build.gradle** file the annotation procesor, if not, @Bind annotations won't work: 

```gradle
dependencies {
  annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
}
```

##Issues 

If you found a bug, or you have an answer, or whatever. Please, open an [issue](https://github.com/BlackBoxVision/mvp-helpers/issues). I will do the best to fix it, or help you. 

##Contributing

Of course, if you see something that you want to upgrade from this library, or a bug that needs to be solved, **PRs are welcome!**

##Release History

**0.0.1**
  * Work in progress

##License

Distributed under the **MIT license**. See [LICENSE](https://github.com/BlackBoxVision/mvp-helpers/blob/kotlin/LICENSE) for more information.
