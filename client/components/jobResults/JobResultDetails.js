import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class JobResultDetails extends Component {

    parseObj(obj) {

        let template = [];

        for (let key in obj) {
            if (obj.hasOwnProperty(key)) {
                if (key === 'id' || key === 'lastModifiedDate' || key === 'jobName') {
                    continue
                } else {
                    template.push(
                        <p>
                            {key}: {obj[key]}
                        </p>
                    )
                }
            }
        }

        return template
    }

    render() {

        return <div>
            {this.props.resultDetails.length ? this.props.resultDetails.map((item, i) => {
                    return <Panel key={i}>
                        <span>{this.parseObj(item)}</span>
                    </Panel>
                }) : <span>Результатов нет</span>}
        </div>
    }
}