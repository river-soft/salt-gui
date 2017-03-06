import React, {Component} from 'react';
import Divider from 'muicss/lib/react/divider';

export default class TreeNodeModalCheckBoxes extends Component {

    constructor(props) {
        super(props);

        this.state = {
            isVisible: false,
            checkedAll: false
        }
    }

    toggleParent() {
        this.setState({isVisible: !this.state.isVisible});
    }

    toggleChild(el, item) {

        let _this = this;

        el.nodeName != 'LI' ? toggleActive(el.parentElement) : toggleActive(el);

        function toggleActive(el) {
            if(el.classList.contains('active')) {
                _this.props.selectItems(item, false);
                _this.setState({checkedAll: false});
            } else {
                _this.props.selectItems(item, true);
            }
        }
    }

    selectAll(group) {

        let items = group.minions || group.scripts;

        for (let i = 0; i < items.length; i++) {
            this.props.selectItems(items[i], true, group);
        }
        this.setState({checkedAll: true});
    }

    unSelectAll(group) {

        let items = group.minions || group.scripts;

        for (let i = 0; i < items.length; i++) {
            this.props.selectItems(items[i], false);
        }
        this.setState({checkedAll: false});
    }

    renderChilds() {
        return this.props.nodes.map((item, index) => {

            let el = this.refs[item.id] || '', className = 'list__item';

            if (el) {
                if (el.classList.contains('active') && this.props.removeItems.length) {
                    for (let i = 0; i < this.props.removeItems.length; i++) {
                        if (this.props.removeItems[i].name === item.name) {
                            className = 'list__item';
                        }
                    }
                }

                for (let i = 0; i < this.props.activeItems.length; i++) {
                    if (this.props.activeItems[i].name === item.name) {
                        className = 'list__item active';
                    }
                }
            }

            return <li key={index} ref={item.id}
                       className={className}
                       onClick={(e) => {
                           ::this.toggleChild(e.target, item, this.props.group.id)
                       }}>
                <span>{item.name}</span>
            </li>
        });
    }

    render() {

        if (!this.props.nodes.length) {
            return null
        }

        return <li className='tree-list__item'>
            <div className='tree-list__item_header'>
            <span
                className={this.state.isVisible ? 'list__header active mui--pull-left' : 'list__header mui--pull-left'}
                onClick={::this.toggleParent}>{this.props.group.group}</span>
                <span
                    className={!this.state.checkedAll ? 'list__header mui--pull-right' : 'list__header hidden mui--pull-right'}
                    onClick={() => {
                        ::this.selectAll(this.props.group);
                    }}><i className='mi mi-add'></i></span>
                <span
                    className={this.state.checkedAll ? 'list__header mui--pull-right' : 'list__header hidden mui--pull-right'}
                    onClick={() => {
                        ::this.unSelectAll(this.props.group);
                    }}><i className='mi mi-remove'></i></span>
            </div>
            <Divider />
            <ul className={this.state.isVisible ? 'list__child mui-list--unstyled' : 'list__child hidden mui-list--unstyled'}>{this.renderChilds()}</ul>
        </li>
    }
}