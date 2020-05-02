(() => {
    const { fromEvent, merge, operators } = rxjs;
    const { map, scan } = operators;

    // map 返回的不是一个值，而是一个 func;
    // scan 的第一个参数是 累计值，第二个参数是 当前值，这个当前值，就是 map 返回的 func

    var increaseButton = document.querySelector("#increase");
    var increase = fromEvent(increaseButton, "click")
        // Again we map to a function the will increase the count
        .pipe(
            map(() => (state) =>
                Object.assign({}, state, { count: state.count + 1 })
            )
        );

    var decreaseButton = document.querySelector("#decrease");
    var decrease = fromEvent(decreaseButton, "click")
        // We also map to a function that will decrease the count
        .pipe(
            map(() => (state) =>
                Object.assign({}, state, { count: state.count - 1 })
            )
        );

    var inputElement = document.querySelector("#input");
    var input = fromEvent(inputElement, "keypress")
        // Let us also map the keypress events to produce an inputValue state
        .pipe(
            map((event) => (state) =>
                Object.assign({}, state, { inputValue: event.target.value })
            )
        );

    // We merge the three state change producing observables
    var state = merge(increase, decrease, input).pipe(
        scan((state, changeFn) => changeFn(state), {
            count: 0,
            inputValue: "",
        })
    );

    // To optimize our rendering we can check what state
    // has actually changed
    var prevState = {};
    state.subscribe((state) => {
        if (state.count !== prevState.count) {
            document.querySelector("#count").innerHTML = state.count;
        }
        if (state.inputValue !== prevState.inputValue) {
            document.querySelector("#hello").innerHTML =
                "Hello " + state.inputValue;
        }
        prevState = state;
    });
})();
