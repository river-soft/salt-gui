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
                    if (groups[j].minions) {
                        groups[j].minions.map((item, index) => {
                            if (item.id === selectedList[i].id) {
                                groups[j].minions.splice(index, 1);
                            }
                        });
                    } else {
                        groups[j].scripts.map((item, index) => {
                            if (item.id === selectedList[i].id) {
                                groups[j].scripts.splice(index, 1);
                            }
                        });
                    }

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

            let items = group.minions || group.scripts;

            for (let i = 0; i < items.length; i++) {
                for (let j = 0; j < this.state.cancelList.length; j++) {
                    if (this.state.cancelList[j].id === items[i].id) {
                        if (this.props.groups[index].minions) {
                            this.props.groups[index].minions.push(group.minions[i]);

                            for (let z = 0; z < this.state.transferedList.length; z++) {
                                if (group.minions[i].id === this.state.transferedList[z].id) {
                                    this.state.transferedList.splice(z, 1);
                                }
                            }
                        } else {
                            this.props.groups[index].scripts.push(items[i]);

                            for (let z = 0; z < this.state.transferedList.length; z++) {
                                if (group.scripts[i].id === this.state.transferedList[z].id) {
                                    this.state.transferedList.splice(z, 1);
                                }
                            }
                        }

                    }
                }
            }
        });

        document.getElementById('transfer__list').childNodes.forEach((el) => {
            if (el.classList.contains('active')) {
                el.classList.remove('active');
            }
        });

        this.setState({
            cancelList: [],
            returnFromTransfer: true
        });
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

    executeScripts(el) {

        if (!el.classList.contains('clicked')) {
            let scripts = [], minions = [];

            if (this.props.minions) {
                scripts.push(this.props.scriptName);

                for (let i = 0; i < this.state.transferedList.length; i++) {
                    minions.push(this.state.transferedList[i].name);
                }
            } else {
                minions.push(this.props.scriptName);

                for (let i = 0; i < this.state.transferedList.length; i++) {
                    scripts.push(this.state.transferedList[i].name);
                }
            }

            this.props.executeScripts(minions, scripts);

            el.classList.add('clicked');
        } else {
            if (this.props.executeError) {
                el.classList.remove('clicked');
                this.executeScripts(el);
            }
        }
    }

    render() {

        let groups = this.props.groups;

        let nodes = groups.length ? groups.map((group, index) => <TreeNodeModalCheckBoxes
                activeItems={this.state.activeItems}
                removeItems={this.state.removeItems}
                nodes={group.scripts || group.minions}
                group={group} key={index} selectItems={::this.selectItems}
                transfer={this.state.transfer}
                transferedList={this.state.transferedList}
                returnFromTransfer={this.state.returnFromTransfer}/>) :
            null;

        return <Row>
            <h5 className='header__center'>{!this.props.minions ? 'Выберите скрипты' : 'Выберите миньоны'}</h5>
            <Col md='4' xs='4' lg='4' className='posr'>
                <div className='select-items'>
                    <ul className='list mui-list--unstyled'>{nodes}</ul>
                </div>
                <div className='add-items-actions mui--align-middle'>
                    <div className='add-items-action' onClick={::this.transferToSelected}><i
                        className='mi mi-arrow-forward'></i></div>
                    <div className='add-items-action' onClick={::this.returnFromTransfer}><i
                        className='mi mi-arrow-back'></i></div>
                </div>
            </Col>
            <Col md='8' xs='8' lg='8'>
                <div className='added-list'>
                    {this.state.transferedList.length ? <ul className='list mui-list--inline' id='transfer__list'>
                            {this.state.transferedList.map((el, index) => {
                                return <li className='list__selected_items' key={index} onClick={(e) => {
                                    ::this.toggleToCancelList(el, e.target);
                                }}>{el.name}</li>
                            })}
                        </ul> : null }
                    {this.props.executeError ?
                        <span className='input_error'>{this.props.executeError.message}</span> : null}
                </div>
                {this.state.transferedList.length ? <div>
                        <button className='button mui-btn mui--pull-right' onClick={(e) => {
                            ::this.executeScripts(e.target);
                        }}>Запустить
                        </button>
                    </div> : null}
            </Col>
        </Row>
    }
}