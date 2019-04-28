function getRequiredComponents()
  return {"position", "player"}
end

function update()
  for i, entity in ipairs(entities) do
    if(game.input:isKeyDown(40)) then
      entity.position.y = entity.position.y + 5
    end
    if(game.input:isKeyDown(38)) then
      entity.position.y = entity.position.y - 5
    end
    if(game.input:isKeyDown(39)) then
      entity.position.x = entity.position.x + 5
    end
    if(game.input:isKeyDown(37)) then
      entity.position.x = entity.position.x - 5
    end
  end
end
  