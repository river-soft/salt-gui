import React, {Component} from 'react';
import ReactDOM from 'react-dom';
// import {store} from '../../index';
// import {Provider} from 'react-redux';

export class Modal extends Component {

    componentDidMount() {
        this.modalTarget = document.createElement('div');
        this.modalTarget.className = 'modal';
        document.body.appendChild(this.modalTarget);
        this._render();
    }

    componentWillUpdate() {
        this._render();
    }

    componentWillUnmount() {
        ReactDOM.unmountComponentAtNode(this.modalTarget);
        document.body.removeChild(this.modalTarget);
    }

    _render() {
        ReactDOM.render(
            <div>{this.props.content}</div>,
            this.modalTarget
        );
    }

    render() {
        return <noscript />;
    }
}
