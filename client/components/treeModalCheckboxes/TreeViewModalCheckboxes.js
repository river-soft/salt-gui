import React, {Component} from 'react';
import TreeNodeModalCheckBoxes from './TreeNodeModalCheckBoxes';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import clone from '../../helpers';

export default class TreeModalCheckboxes extends Component {

    constructor(props) {
        super(props);

        this.state = {
            groups: [],
            isVisible: false,
            selectedList: [],
            groupList: [],
            activeItems: [],
            removeItems: [],
            transferedList: [],
            transfer: false,
            cancelList: []
        }
    }

    componentDidUpdate() {

        if (!this.state.groups.length && this.props.groups.length) {
            this.state.groups = clone(this.props.groups);
        }
    }

    selectItems(item, add) {

        if (!add) {
            if (this.state.selectedList.length) {
                for (let i = 0; i < this.state.selectedList.length; i++) {
                    if (this.state.selectedList[i].name === item.name) {
                        this.state.selectedList.splice(i, 1);
                        this.state.removeItems.push(item);
                    }
                }

                if (this.state.activeItems.length) {
                    for (let i = 0; i < this.state.activeItems.length; i++) {
                        if (this.state.activeItems[i].name === item.name) {
                            this.state.activeItems.splice(i, 1);
                        }
                    }
                }
            }
        } else {
            if (this.state.selectedList.length) {

                let counter = 0;

                for (let i = 0; i < this.state.selectedList.length; i++) {
                    if (this.state.selectedList[i].name != item.name) {
                        counter++;
                    }
                }

                if (counter === this.state.selectedList.length) {
                    this.state.selectedList.push(item);
                    this.state.activeItems.push(item);
                }

                if (this.state.removeItems.length) {
                    for (let i = 0; i < this.state.removeItems.length; i++) {
                        if (this.state.removeItems[i].name === item.name) {
                            this.state.removeItems.splice(i, 1);
                        }
                    }
                }
            } else {
                this.state.selectedList.push(item);
                this.state.activeItems.push(item);
            }
        }

        this.setState({go: true});
    }

    transferToSelected() {
        let _this = this,
            groups = _this.props.groups,
            selectedList = _this.state.selectedList;

        if (selectedList.length) {
            for (let i = 0; i < selectedList.length; i++) {
                for (let j = 0; j < groups.length; j++) {
                    groups[j].minions.map((item, index) => {
                        if (item.id === selectedList[i].id) {
                            groups[j].minions.splice(index, 1);
                        }
                    });
                }
            }

            selectedList.map((el) => {
                _this.state.transferedList.push(el)
            });

            _this.setState({
                activeItems: [],
                selectedList: [],
                transfer: true
            });
        }
    }

    returnFromTransfer() {
        this.state.groups.map((group, index) => {
            for (let i = 0; i < group.minions.length; i++) {
                for (let j = 0; j < this.state.cancelList.length; j++) {
                    if (this.state.cancelList[j].id === group.minions[i].id) {
                        this.props.groups[index].minions.push(group.minions[i]);

                        for(let z = 0; z < this.state.transferedList.length; z++) {
                            if(group.minions[i].id === this.state.transferedList[z].id) {
                                this.state.transferedList.splice(z, 1);
                            }
                        }
                    }
                }
            }
        });

        document.getElementById('transfer__list').childNodes.forEach((el) => {
            if(el.classList.contains('active')) {
                el.classList.remove('active');
            }
        });

        this.setState({cancelList: []});
    }

    toggleToCancelList(item, el) {
        if (el.classList.contains('active')) {
            for (let i = 0; i < this.state.cancelList.length; i++) {
                if (this.state.cancelList[i].id === item.id) {
                    this.state.cancelList.splice(i, 1);
                }
            }
            el.classList.remove('active');
        } else {

            this.state.cancelList.push(item);
            el.classList.add('active');
        }

        this.setState({go: true});
    }

    executeScripts() {

        let list = [];
        let list2 = [];

        list2.push(String(this.props.scriptName));

        for(let i = 0; i < this.state.transferedList.length; i++) {
            list.push(String(this.state.transferedList[i].name));
        }

        this.props.executeScripts(list, list2);
    }

    render() {

        let groups = this.props.groups;

        let nodes = groups.length ? groups.map((group, index) => <TreeNodeModalCheckBoxes
                activeItems={this.state.activeItems}
                removeItems={this.state.removeItems}
                nodes={group.scripts || group.minions}
                group={group} key={index} selectItems={::this.selectItems}
                transfer={this.state.transfer}
                transferedList={this.state.transferedList}/>) : null;

        return <Row>
            <Col md='4' xs='4' lg='4'>
                <div className='select-items'>
                    <ul className='list mui-list--unstyled'>{nodes}</ul>
                    <div className='add-items-actions'>
                        <div className='add-items-action' onClick={::this.transferToSelected}><i
                            className='mi mi-arrow-forward'></i></div>
                        <div className='add-items-action' onClick={::this.returnFromTransfer}><i
                            className='mi mi-arrow-back'></i></div>
                    </div>
                </div>
            </Col>
            <Col md='8' xs='8' lg='8'>
                <div className='added-list'>
                    <ul className='list mui-list--inline' id='transfer__list'>
                        {this.state.transferedList.length ? this.state.transferedList.map((el, index) => {
                                return <li className='list__selected_items' key={index} onClick={(e) => {
                                    ::this.toggleToCancelList(el, e.target);
                                }}>{el.name}</li>
                            }) : null}
                    </ul>
                </div>
                {this.state.transferedList.length ? <div>
                        <button className='button mui-btn mui--pull-right' onClick={() => {
                            ::this.executeScripts();
                        }}>run</button>
                    </div> : null}

            </Col>
        </Row>
    }
}