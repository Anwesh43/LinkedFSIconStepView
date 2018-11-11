## FSIconStepView

### Usage

#### Creating View in Activity

```
    val view : FSIconStepView = FSIconStepView.create(this)
```

#### Adding OnAnimationListener to activity

```
    view.addOnAnimationListener({i ->
        //do something on animation complete
    }, {i ->
        //do something when aninmation is reset to original state
    })
```

### Demo

<img src="https://github.com/Anwesh43/LinkedFSIconStepView/blob/master/demo/fsiconstepview.gif"  width="300px" height="500px">