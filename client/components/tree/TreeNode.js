import React, {Component} from 'react';
import Divider from 'muicss/lib/react/divider';
import {containsRole} from '../../helpers';

export default class TreeNode extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isVisible: false,
            selected: '',
            setted: false,
            clicked: false
        };
    }

    componentWillUpdate() {
        if (this.props.createdGroup) {
            if (this.props.createdGroup.group === this.props.group.group) {
                this.state.isVisible = true;
                this.state.setted = true;
            }
        }
    }

    toggle() {
        this.setState({
            isVisible: !this.state.isVisible,
            clicked: true
        });
    }

    setFilter(filter) {

        if (this.props.selected != filter) {
            let items = document.querySelectorAll('.list__child .list__item');
            for (let i = 0; i < items.length; i++) {
                if (items[i].classList.contains('active')) {
                    items[i].classList.remove('active');
                }
            }

            this.props.setSelected(filter);
        }
    }

    isActive(value) {
        return 'list__item' + ((value === this.props.selected) ? ' active' : '')
    }

    render() {

        if (!this.state.setted && !this.state.clicked) {
            this.state.isVisible = this.props.rerender;
        }

        let items = this.props.nodes.sort((a, b) => {
            if (a.name.toLowerCase() > b.name.toLowerCase()) {
                return 1
            }
            if (a.name.toLowerCase() < b.name.toLowerCase()) {
                return -1
            }
            return 0
        });

        let nodes = items.map((file, index) => {
            return <li className={this.isActive(file.name)} key={index} onClick={() => {
                typeof this.props.showContent === 'function' ? this.props.showContent(file.id, this.props.user, file.name) : null;
                this.setFilter(file.name);
            }}>{file.name}</li>
        }, this);

        let removeGroup, messages = this.props.messages;

        if (containsRole(this.props.user.roles, this.props.permittedRoles.delete)) {

            if (this.props.removeIfNotEmpty) {
                removeGroup = <span className='tree-list__item_action' onClick={() => {
                    this.props.removeGroup(this.props.group.id, this.props.group.group, this.props.nodes.length);
                }} title={messages['client.btn.delete']}><i className='mi mi-delete'></i></span>
            } else {
                if (!this.props.nodes.length) {
                    removeGroup = <span className='tree-list__item_action' onClick={() => {
                        this.props.removeGroup(this.props.group.id, this.props.group.group, this.props.nodes.length);
                    }} title={messages['client.btn.delete']}><i className='mi mi-delete'></i></span>
                }
            }
        }

        return <li className='tree-list__item'>
            <div className='tree-list__item_actions'>

                {containsRole(this.props.user.roles, this.props.permittedRoles.edit) ?
                    <span className='tree-list__item_action' onClick={() => {
                        this.props.editGroup(this.props.group.id, this.props.group.group);
                    }} title={messages['client.btn.edit']}><i className='mi mi-create'></i></span> : null
                }
                {removeGroup}
            </div>
            <span className={this.state.isVisible ? 'list__header active' : 'list__header'}
                  onClick={::this.toggle}>{this.props.group.group}</span>
            <Divider />
            <ul className={this.state.isVisible ? 'list__child mui-list--unstyled' : 'list__child hidden mui-list--unstyled'}>{nodes}</ul>
        </li>
    }
}
