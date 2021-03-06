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
import * as editMinionsGroupAction from '../actions/EditMinionsGroupAction';
import * as getGroupsByMinionAction from '../actions/GetGroupsByMinionAction';
import * as editMinionGroupsAction from '../actions/EditMinionGroupsAction';
import * as filesTreeActions from '../actions/FilesTreeActions';
import * as executeScriptsAction from '../actions/ExecuteScriptsAction';
import * as getMessagesAction from '../actions/GetMessagesAction';
import TreeView from '../components/tree/TreeView';
import MinionDetails from '../components/minions/MinionDetails';
import CreateMinionsGroupModal from '../components/minions/CreateMinionsGroupModal';
import RemoveMinionsGroupModal from '../components/minions/RemoveMinionsGroupModal';
import EditMinionsGroupModal from '../components/minions/EditMinionsGroupModal';
import EditMinionGroupsModal from '../components/minions/EditMinionGroupsModal';
import Modal from 'react-modal';
import TreeViewModalCheckboxes from '../components/treeModalCheckboxes/TreeViewModalCheckboxes';
import {containsRole, changeLanguage} from '../helpers';
import cookie from 'react-cookie';

class GroupsAndMinions extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterMinions: [],
            removedGroup: {},
            editedGroup: {},
            minionDescriptionName: '',
            rerender: false,
            showMinionDescription: false,
            showModal: false,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: false,
            editMinionsGroupModal: false,
            editMinionGroupsModal: false,
            runScript: false,
            minionName: '',
            strings: ''
        }
    }

    componentWillMount() {
        if (!this.props.localization) {
            const {getMessages} = this.props.getMessagesAction;

            getMessages();
        } else {
            this.setState({strings: this.props.localization.messages});
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

        if (this.props.editMinionsGroup.edit) {
            this.props.editMinionsGroup.edit = false;
            this.onRequestClose();
            getGroupedMinions();
        }

        if (this.props.editMinionGroups.editGroup) {
            this.props.getGroupsByMinion.groups = [];
            this.props.editMinionGroups.editGroup = false;
            this.onRequestClose();
            getGroupedMinions();
        }

        if (this.props.executeScripts.execute && this.state.runScript) {
            this.setState({runScript: false});
        } else if (!this.state.runScript) {
            this.props.executeScripts.execute = false;
        }
    }

    setLanguage(locale) {
        changeLanguage(locale);
        this.state.strings.setLanguage(locale);
        this.setState({changeLocale: true});
    }

    showContent(minionId, user, minionName) {

        if (containsRole(user.roles, ['ROLE_SHOW_MINION_DETAILS', 'ROLE_ROOT'])) {
            const {getMinionDetails} = this.props.minionDetailsAction;

            getMinionDetails(minionName);

            if(this.props.minionDetails.minionDetails) {
                this.props.minionDetails.minionDetails = '';
            }

            if(this.props.minionDetails.error) {
                this.props.minionDetails.error = '';
            }

            this.setState({
                showMinionDescription: true,
                minionDescriptionName: minionName,
                runScript: false
            })
        }
    }

    filterTree(e) {
        let obj = [],
            groupedMinions = this.props.groupedMinions.groupedMinions;

        if (e.target.value) {
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
        } else {
            this.setState({
                filterMinions: [],
                rerender: false
            })
        }
    }

    createGroup() {
        this.setState({
            showModal: true,
            createMinionsGroupModal: true,
            removeMinionsGroupModal: false,
            editMinionsGroupModal: false,
            editMinionGroupsModal: false
        })
    }

    removeGroup(groupId, groupName, minionsSize) {
        this.setState({
            showModal: true,
            createMinionsGroupModal: false,
            editMinionsGroupModal: false,
            editMinionGroupsModal: false,
            removeMinionsGroupModal: true,
            removedGroup: {
                id: groupId,
                name: groupName,
                size: minionsSize
            }
        })
    }

    editGroup(groupId, groupName) {
        this.setState({
            showModal: true,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: false,
            editMinionGroupsModal: false,
            editMinionsGroupModal: true,
            editedGroup: {
                id: groupId,
                name: groupName
            }
        })
    }

    editMinionGroups(minionName) {
        const {getGroupsByMinion} = this.props.getGroupsByMinionAction;
        getGroupsByMinion(minionName);

        this.setState({
            showModal: true,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: false,
            editMinionsGroupModal: false,
            editMinionGroupsModal: true,
            minionDescriptionName: minionName
        });
    }

    runScript(minionName) {
        this.setState({
            runScript: true,
            showModal: false,
            createMinionsGroupModal: false,
            removeMinionsGroupModal: false,
            editMinionsGroupModal: false,
            editMinionGroupsModal: false,
            showMinionDescription: false,
            minionName: minionName
        });

        const {filesRequest} = this.props.filesTreeActions;
        filesRequest();
    }

    onRequestClose() {
        this.props.removeMinionsGroup.error = false;
        this.setState({showModal: false});
    }

    render() {

        let token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        let permittedRoles = {
            edit: ['ROLE_EDIT_MINIONS_GROUP', 'ROLE_ROOT'],
            delete: ['ROLE_DELETE_MINIONS_GROUP', 'ROLE_ROOT']
        };

        const {createMinionsGroup} = this.props.createMinionsGroupAction,
            {removeMinionsGroup} = this.props.removeMinionsGroupAction,
            {editMinionsGroup} = this.props.editMinionsGroupAction,
            {editMinionGroups} = this.props.editMinionGroupsAction,
            {executeScripts} = this.props.executeScriptsAction;

        let treeView, modal, executeError = this.props.executeScripts.error,
            minionsDetailsError = this.props.minionDetails.error,
            messages = this.state.strings;

        if (this.props.groupedMinions.groupedMinions.length === 0) {
            treeView = <div>{messages['client.messages.no.data']}</div>
        } else if (this.state.rerender) {

            treeView = <TreeView groups={this.state.filterMinions} showContent={::this.showContent}
                                 editGroup={::this.editGroup} messages={messages}
                                 removeGroup={::this.removeGroup}
                                 removeIfNotEmpty={true}
                                 rerender={true}
                                 permittedRoles={permittedRoles}
                                 user={user}/>;
        } else {
            treeView = <TreeView groups={this.props.groupedMinions.groupedMinions} showContent={::this.showContent}
                                 editGroup={::this.editGroup} messages={messages}
                                 removeGroup={::this.removeGroup}
                                 removeIfNotEmpty={true}
                                 rerender={false}
                                 permittedRoles={permittedRoles}
                                 user={user}/>;
        }

        if (this.state.showModal) {
            if (this.state.createMinionsGroupModal) {
                modal = <CreateMinionsGroupModal groups={this.props.groupedMinions.groupedMinions}
                                                 createMinionsGroup={createMinionsGroup} messages={messages}
                                                 closeModal={::this.onRequestClose}/>
            } else if (this.state.removeMinionsGroupModal) {
                modal = <RemoveMinionsGroupModal group={this.state.removedGroup} closeModal={::this.onRequestClose}
                                                 removeGroup={removeMinionsGroup} messages={messages}
                                                 error={this.props.removeMinionsGroup.error}/>
            } else if (this.state.editMinionsGroupModal) {
                modal = <EditMinionsGroupModal group={this.state.editedGroup} closeModal={::this.onRequestClose}
                                               groups={this.props.groupedMinions.groupedMinions} messages={messages}
                                               edit={editMinionsGroup}/>
            } else if (this.state.editMinionGroupsModal) {
                modal = <EditMinionGroupsModal closeModal={::this.onRequestClose}
                                               groups={this.props.getGroupsByMinion.groups}
                                               minion={this.state.minionDescriptionName}
                                               edit={editMinionGroups} messages={messages}/>
            }
        }

        return <div className='wrapper'>
            <Header header={messages['client.header.minions.groups.title']} messages={messages}
                    setLanguage={::this.setLanguage}/>
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='6' xs='12' lg='3'>

                            {containsRole(user.roles, ['ROLE_SHOW_GROUPED_MINIONS', 'ROLE_ROOT']) ?
                                <div>
                                    <Input label={messages['client.input.search.minions']} floatingLabel={true}
                                           onChange={e => {
                                               this.filterTree(e)
                                           }}/>
                                    <ul className='list mui-list--unstyled'>
                                        {treeView}
                                    </ul>
                                </div> : null}

                            {containsRole(user.roles, ['ROLE_CREATE_MINIONS_GROUP', 'ROLE_ROOT']) ?
                                <button className='mui-btn button'
                                        onClick={::this.createGroup}>{messages['client.minions.btn.add.group']}</button> : null}
                        </Col>
                        <Col md='6' xs='12' lg='9'>
                            {this.state.showMinionDescription ?
                                <MinionDetails minionName={this.state.minionDescriptionName}
                                               details={this.props.minionDetails.minionDetails[0]}
                                               getGroups={::this.editMinionGroups}
                                               runScript={::this.runScript} messages={messages}
                                               error={minionsDetailsError}
                                               user={user}/>
                                : null}
                            {this.state.runScript ?
                                <TreeViewModalCheckboxes groups={this.props.filesTree.files}
                                                         scriptName={this.state.minionName}
                                                         messages={messages}
                                                         executeScripts={executeScripts}
                                                         executeError={executeError}
                                                         minions={false}/> : null}
                            {this.props.executeScripts.execute ?
                                <span
                                    className='success-mess'>{messages['client.message.scripts.run.success']}</span> : null}
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
        removeMinionsGroup: state.removeMinionsGroup,
        editMinionsGroup: state.editMinionsGroup,
        getGroupsByMinion: state.getGroupsByMinion,
        editMinionGroups: state.editMinionGroups,
        filesTree: state.filesTree,
        executeScripts: state.executeScripts,
        localization: state.localization
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getGroupedMinionsAction: bindActionCreators(getGroupedMinionsAction, dispatch),
        minionDetailsAction: bindActionCreators(minionDetailsAction, dispatch),
        createMinionsGroupAction: bindActionCreators(createMinionsGroupAction, dispatch),
        removeMinionsGroupAction: bindActionCreators(removeMinionsGroupAction, dispatch),
        editMinionsGroupAction: bindActionCreators(editMinionsGroupAction, dispatch),
        getGroupsByMinionAction: bindActionCreators(getGroupsByMinionAction, dispatch),
        editMinionGroupsAction: bindActionCreators(editMinionGroupsAction, dispatch),
        filesTreeActions: bindActionCreators(filesTreeActions, dispatch),
        executeScriptsAction: bindActionCreators(executeScriptsAction, dispatch),
        getMessagesAction: bindActionCreators(getMessagesAction, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GroupsAndMinions)