import React, {Component} from 'react';
import TreeNodeModalCheckBoxes from './TreeNodeModalCheckBoxes';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';

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
            this.setState({
                groups: this.props.groups
            })
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

        let _this = this;

        for (let i = 0; i < _this.state.selectedList.length; i++) {
            for (let j = 0; j < _this.state.groups.length; j++) {
                _this.state.groups[j].minions.map((item, index) => {
                    if (item.id === _this.state.selectedList[i].id) {
                        _this.state.groups[j].minions.splice(index, 1);
                    }
                });
            }
        }

        _this.state.selectedList.map((el) => {
            _this.state.transferedList.push(el)
        });

        _this.setState({
            selectedList: [],
            transfer: true
        });
    }

    returnFromTransfer() {

        this.props.groups.map((group, index) => {
            for (let i = 0; i < group.minions.length; i++) {
                for(let j = 0; j < this.state.cancelList.length; j++) {
                    if(this.state.cancelList[j].id === group.minions[i].id) {
                        this.state.groups[index].minions.add(group.minions[i]);
                    }
                }
            }
        });
        console.log(this.state.groups);
        console.log(this.state.cancelList);
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

    render() {

        let groups = this.state.groups;

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
                    <ul className='list mui-list--inline'>
                        {this.state.transferedList.length ? this.state.transferedList.map((el, index) => {
                                return <li className='list__selected_items' key={index} onClick={(e) => {
                                    ::this.toggleToCancelList(el, e.target);
                                }}>{el.name}</li>
                            }) : null}
                    </ul>
                </div>
            </Col>
        </Row>
    }
}