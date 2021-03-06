import React, {Component} from 'react';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';
import Checkbox from 'muicss/lib/react/checkbox';

export default class EditMinionGroupsModal extends Component {

    constructor(props) {
        super(props);

        this.state = {
            groupsList: [],
            update: false,
            generated: false
        }
    }

    componentDidUpdate() {
        if(this.props.groups && this.props.groups.length && !this.state.generated) {
            this.props.groups.map((item) => {
                if(item.checked) {
                    this.state.groupsList.push({
                        id: item.id,
                        name: item.name
                    });
                }
            });
            this.setState({generated: true});
        }
    }

    addToGroupsList(e, groupName, groupId) {
        if (e.target.checked) {
            this.state.groupsList.push({
                id: groupId,
                name: groupName
            })
        } else {
            let index, groups = this.state.groupsList;
            for (let i = 0; i < groups.length; i++) {
                if (groups[i].name === groupName) {
                    index = i;
                }
            }
            this.state.groupsList.splice(index, 1);
        }
        this.setState({update: true});
    }

    editMinionGroups() {
        let obj = {
            name: this.props.minion,
            groups: this.state.groupsList
        };

        this.props.edit(obj);
    }


    render() {

        let messages = this.props.messages;

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <h4 className='mui--text-center modal__header'>{messages['client.modal.minions.group.edit.title']}</h4>
            <div className='modal__body'>
                <div className='block-list'>
                    <h6 className='block-list__header'>{messages['client.modal.minions.select.groups']}</h6>
                    {this.props.groups.length ? this.props.groups.map((item, index) => {
                            return <Checkbox label={item.name} key={index} onChange={e => {
                                ::this.addToGroupsList(e, item.name, item.id)
                            }} defaultChecked={item.checked}/>
                        }) : <span>{messages['client.messages.no.data']}</span>}
                </div>
            </div>
            <div className='modal__footer'>
                <Divider />
                <Button size='small' color='primary' variant='flat'
                        className='modal__btn mui--pull-right' onClick={::this.editMinionGroups}
                        disabled={!this.state.groupsList.length}>{messages['client.btn.save']}</Button>
            </div>
        </div>
    }
}