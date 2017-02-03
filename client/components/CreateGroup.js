import React, {Component} from 'react';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import TextArea from 'muicss/lib/react/textarea';

export default class CreateGroup extends Component {

    constructor(props) {
        super(props);

        this.state = {
            addScript: false,
            groupName: '',
            scriptName: '',
            scriptContent: '',
            scripts: [],
        };

        this.addScript = this.addScript.bind(this);
        this.createModel = this.createModel.bind(this);
    }

    addScript() {
        this.setState({
            addScript: true
        });
    }

    setScriptName(name) {
        this.setState({scriptName: name});
    }

    setScriptContent(content) {
        this.setState({scriptContent: content});
    }

    setGroupName(name) {
        this.setState({groupName: name});
    }

    addScriptForm(id) {
        return <div className={id}><Input label='Название скрипта' floatingLabel={true} onChange={(e) => {
            this.setScriptName(e.target.value);
        }}/>
            <TextArea label='Текст скрипта' floatingLabel={true} onChange={(e) => {
                this.setScriptContent(e.target.value)
            }}/>
            <span onClick={() => {
                this.addScript();
                this.createScriptStore();
            }}>Добавить</span>
        </div>
    }

    createScriptStore() {
        this.state.scripts.push({
            name: this.state.scriptName,
            content: this.state.scriptContent
        });

        this.setState({addScript: false});
    }

    createModel() {
        let obj = {
            group: this.state.groupName,
            scripts: []
        };

        this.state.scripts.map((script) => {
           obj.scripts.push({
               name: script.name,
               content: script.content
           })
        });

        console.log('Object:', obj);
        this.props.createGroup(obj);
    }

    render() {

        return <div>
            <h4 className='mui--text-center'>Создание группы и скриптов</h4>
            <Form>
                <Input label='Название группы' floatingLabel={true} name='group' onChange={(e) => {this.setGroupName(e.target.value)}} />
                {this.state.scripts.length > 0 ? this.state.scripts.map((script, index) => {
                    return <div key={index}>{script.name}</div>
                    }) : null}
                {this.state.addScript ? null :
                    <span className='mui--text-right' onClick={this.addScript.bind(this)}>Добавить скрипт</span>}
                {this.state.addScript ? this.addScriptForm(new Date().getTime()) : null}
            </Form>
            <button onClick={this.createModel}>Собрать</button>
        </div>
    }
}