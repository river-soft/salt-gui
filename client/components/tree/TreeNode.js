import React, {Component} from 'react';
import Divider from 'muicss/lib/react/divider';
import $ from 'jquery';
export default class TreeNode extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isVisible: false,
            selected: ''
        };
    }

    toggle() {
        this.setState({isVisible: !this.state.isVisible});
    }

    setFilter(filter) {
        $('.list__child .list__item').removeClass('active');
        this.setState({selected: filter});
    }

    isActive(value) {
        return 'list__item' + ((value===this.state.selected) ? ' active' : '')
    }

    render() {

        let nodes = this.props.files.map((file, index) => {
            return <li className={this.isActive(file.name)} key={index} onClick={() => {
                this.props.showContent(file.content);
                this.setFilter(file.name);
            }}>{file.name}</li>
        }, this);

        return <li>
            <span className={this.state.isVisible ? 'list__header active' : 'list__header'} onClick={this.toggle.bind(this)}>{this.props.group.name}</span>
            <Divider />
            <ul className={this.state.isVisible ? 'list__child mui-list--unstyled' : 'list__child hidden mui-list--unstyled'}>{nodes}</ul>
        </li>
    }
}