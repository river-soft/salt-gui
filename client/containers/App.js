import React, {Component} from 'react';
import {connect} from 'react-redux';

class App extends Component {
    render() {
        return <div>Привет из App!!!</div>
    }
}

function mapStateToProps(/*state*/) {
    return {}
}

function mapDispatchToProps(/*disptch*/) {
    return {}
}

export default connect(mapStateToProps, mapDispatchToProps)(App);