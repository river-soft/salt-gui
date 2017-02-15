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
        if(this.state.selected != filter) {
            $('.list__child .list__item').removeClass('active');
            this.setState({selected: filter});
        }
    }

    isActive(value) {
        return 'list__item' + ((value===this.state.selected) ? ' active' : '')
    }

    render() {

        let nodes = this.props.nodes.map((file, index) => {
            return <li className={this.isActive(file.name)} key={index} onClick={() => {
                typeof this.props.showContent === 'function' ? this.props.showContent(file.id, file.name) : null;
                this.setFilter(file.name);
            }}>{file.name}</li>
        }, this);

        return <li className='tree-list__item'>
            <div className='tree-list__item_actions'>
                <span className='tree-list__item_action'><i className='mi mi-create'></i></span>
                <span className='tree-list__item_action' onClick={() => {
                    this.props.removeGroup(this.props.group.id);
                }}><i className='mi mi-delete'></i></span>
            </div>
            <span className={this.state.isVisible ? 'list__header active' : 'list__header'} onClick={::this.toggle}>{this.props.group.group}</span>
            <Divider />
            <ul className={this.state.isVisible ? 'list__child mui-list--unstyled' : 'list__child hidden mui-list--unstyled'}>{nodes}</ul>
        </li>
    }
}