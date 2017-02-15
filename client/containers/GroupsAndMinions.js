import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Row from 'muicss/lib/react/row';
import Col from 'muicss/lib/react/col';
import Input from 'muicss/lib/react/input';
import Container from 'muicss/lib/react/container';
import * as getGroupedMinionsAction from '../actions/GetGroupedMinionsAction';
import * as minionDetailsAction from '../actions/MinionDetailsAction';
import * as createMinionsGroupAction from '../actions/CreateMinionsGroupAction';
import * as removeMinionsGroupAction from '../actions/RemoveMinionsGroupAction';
import TreeView from '../components/tree/TreeView';
import MinionDetails from '../components/minions/MinionDetails';
import CreateMinionsGroupModal from '../components/minions/CreateMinionsGroupModal';
import RemoveMinionsGroupModal from '../components/minions/RemoveMinionsGroupModal';
import Modal from 'react-modal';

class GroupsAndMinions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterMinions: [],
            removedGroup: {},
            rerender: false,
            showMinionDescription: false,
            showModal: false,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: false
        }
    }

    componentDidMount() {
        const {getGroupedMinions} = this.props.getGroupedMinionsAction;
        if (typeof getGroupedMinions === 'function') {
            getGroupedMinions();
        }
    }

    componentDidUpdate() {
        const {getGroupedMinions} = this.props.getGroupedMinionsAction;

        if (this.props.createMinionsGroup.createGroup) {
            this.props.createMinionsGroup.createGroup = false;
            this.onRequestClose();
            getGroupedMinions();
        }

        if (this.props.removeMinionsGroup.remove) {
            this.props.removeMinionsGroup.remove = false;
            this.onRequestClose();
            getGroupedMinions();
        }
    }

    showContent(minionId, minionName) {
        const {getMinionDetails} = this.props.minionDetailsAction;

        getMinionDetails(minionName);
        this.setState({showMinionDescription: true})
    }

    filterTree(e) {
        let obj = [],
            groupedMinions = this.props.groupedMinions.groupedMinions;

        for (let i = 0; i < groupedMinions.length; i++) {

            let minions = groupedMinions[i].minions.filter((item) => {
                return item.name.toLowerCase().search(e.target.value.toLowerCase()) !== -1
            });

            if (minions.length > 0) {
                obj.push({
                    group: groupedMinions[i].group,
                    minions: minions
                })
            }
        }

        this.setState({
            filterMinions: obj,
            rerender: true
        });
    }

    createMinionsGroup() {
        this.setState({
            showModal: true,
            createMinionsGroupModal: true,
            removeMinionsGroupModal: false
        })
    }

    removeGroup(groupId, groupName, minionsSize) {
        this.setState({
            showModal: true,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: true,
            removedGroup: {
                id: groupId,
                name: groupName,
                size: minionsSize
            }
        })
    }

    onRequestClose() {
        this.props.removeMinionsGroup.error = false;
        this.setState({showModal: false});
    }

    render() {

        const {createMinionsGroup} = this.props.createMinionsGroupAction,
            {removeMinionsGroup} = this.props.removeMinionsGroupAction;
        let treeView, modal;

        if (this.props.groupedMinions.groupedMinions.length === 0) {
            treeView = <div>Данных нету</div>
        } else if (this.state.rerender) {
            treeView = <TreeView groups={this.state.filterMinions} showContent={::this.showContent}
                                 removeGroup={::this.removeGroup}/>;
        } else {
            treeView = <TreeView groups={this.props.groupedMinions.groupedMinions} showContent={::this.showContent}
                                 removeGroup={::this.removeGroup}/>;
        }

        if (this.state.showModal) {
            if (this.state.createMinionsGroupModal) {
                modal = <CreateMinionsGroupModal groups={this.props.groupedMinions.groupedMinions}
                                                 createMinionsGroup={createMinionsGroup}
                                                 closeModal={::this.onRequestClose}/>
            } else if (this.state.removeMinionsGroupModal) {
                modal = <RemoveMinionsGroupModal group={this.state.removedGroup} closeModal={::this.onRequestClose}
                                                 removeGroup={removeMinionsGroup}
                                                 error={this.props.removeMinionsGroup.error}/>
            }
        }

        return <div className='wrapper'>
            <Header />
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='3' xs='6' lg='3'>
                            <Input label='Поиск' floatingLabel={true} onChange={e => {
                                this.filterTree(e)
                            }}/>
                            <ul className='list mui-list--unstyled'>
                                {treeView}
                            </ul>
                            <button className='mui-btn button' onClick={::this.createMinionsGroup}>добавить</button>
                        </Col>
                        <Col md='9' xs='6' lg='9'>
                            {this.state.showMinionDescription ?
                                <MinionDetails details={this.props.minionDetails.minionDetails[0]}/>
                                : null}
                        </Col>
                    </Row>
                </Container>
            </main>
            <Modal contentLabel='label' isOpen={this.state.showModal} className='modal'
                   onRequestClose={::this.onRequestClose} overlayClassName='overlay'
                   parentSelector={() => document.body} ariaHideApp={false}>
                {modal}
            </Modal>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        groupedMinions: state.groupedMinions,
        minionDetails: state.minionDetails,
        createMinionsGroup: state.createMinionsGroup,
        removeMinionsGroup: state.removeMinionsGroup
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getGroupedMinionsAction: bindActionCreators(getGroupedMinionsAction, dispatch),
        minionDetailsAction: bindActionCreators(minionDetailsAction, dispatch),
        createMinionsGroupAction: bindActionCreators(createMinionsGroupAction, dispatch),
        removeMinionsGroupAction: bindActionCreators(removeMinionsGroupAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupsAndMinions)