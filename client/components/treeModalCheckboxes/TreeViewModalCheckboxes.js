import React, {Component} from 'react';
import TreeNodeModalCheckBoxes from './TreeNodeModalCheckBoxes';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import clone from '../../helpers';
import Input from 'muicss/lib/react/input';

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
            cancelList: [],
            filterItems: [],
            reserveGroups: []
        }
    }

    componentDidUpdate() {

        if (!this.state.groups.length && this.props.groups.length) {
            this.state.reserveGroups = clone(this.props.groups);
            this.state.groups = clone(this.props.groups);
        }
    }

    filterTree(e) {
        let obj = [], _this = this,
            groups = this.props.groups;

        if (e.target.value) {
            for (let i = 0; i < groups.length; i++) {

                let items = [];

                if (_this.props.minions) {
                    items = groups[i].minions.filter((item) => {

                        let count = 0;

                        for (let j = 0; j < _this.state.transferedList.length; j++) {
                            if (_this.state.transferedList[j].name === item.name) {
                                count++
                            }
                        }

                        if (count > 0) {
                            return false;
                        } else {
                            return item.name.toLowerCase().search(e.target.value.toLowerCase()) !== -1
                        }
                    });
                } else {
                    items = groups[i].scripts.filter((item) => {

                        let count = 0;

                        for (let j = 0; j < _this.state.transferedList.length; j++) {
                            if (_this.state.transferedList[j].name === item.name) {
                                count++
                            }
                        }

                        if (count > 0) {
                            return false;
                        } else {
                            return item.name.toLowerCase().search(e.target.value.toLowerCase()) !== -1
                        }
                    });
                }

                if (items.length > 0) {
                    obj.push({
                        group: groups[i].group,
                        minions: items
                    })
                }
            }

            _this.setState({
                filterItems: obj,
                groups: clone(obj),
                rerender: true
            });
        } else {

            if (_this.state.transferedList.length) {
                for (let i = 0; i < _this.state.transferedList.length; i++) {
                    for (let j = 0; j < groups.length; j++) {
                        if (groups[j].minions) {
                            groups[j].minions.map((item, index) => {
                                if (item.id === _this.state.transferedList[i].id) {
                                    groups[j].minions.splice(index, 1);
                                }
                            });
                        } else {
                            groups[j].scripts.map((item, index) => {
                                if (item.id === _this.state.transferedList[i].id) {
                                    groups[j].scripts.splice(index, 1);
                                }
                            });
                        }
                    }
                }
            }

            _this.setState({
                groups: clone(_this.state.reserveGroups),
                filterItems: [],
                rerender: false
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
        let _this = this,
            groups = _this.state.rerender ? _this.state.filterItems : _this.props.groups,
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
                _this.state.transferedList.push(el);
            });

            _this.setState({
                activeItems: [],
                selectedList: [],
                transfer: true
            });
        }
    }

    returnFromTransfer() {

        let groups = this.state.rerender ? this.state.filterItems : this.props.groups;

        this.state.groups.map((group, index) => {

            let items = group.minions || group.scripts;

            for (let i = 0; i < items.length; i++) {
                for (let j = 0; j < this.state.cancelList.length; j++) {
                    if (this.state.cancelList[j].id === items[i].id) {
                        if (groups[index].minions) {
                            groups[index].minions.push(group.minions[i]);

                            for (let z = 0; z < this.state.transferedList.length; z++) {
                                if (group.minions[i].id === this.state.transferedList[z].id) {
                                    this.state.transferedList.splice(z, 1);
                                }
                            }
                        } else {
                            groups[index].scripts.push(items[i]);

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

        let groups = this.state.rerender ? this.state.filterItems : this.props.groups;

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
            {/*<h5 className='header__center'>{!this.props.minions ? 'Выберите скрипты' : 'Выберите миньоны'}</h5>*/}
            <Col md='6' xs='12' lg='4' className='posr'>
                <Input label={this.props.minions ? 'Поиск миньонов' : 'Поиск скриптов'} floatingLabel={true}
                       onChange={::this.filterTree}/>
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
            <Col md='6' xs='12' lg='8'>
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